package todoktodok.backend.discussion.application.service.query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.comment.domain.repository.CommentRepository;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.DiscussionFilterType;
import todoktodok.backend.discussion.domain.repository.DiscussionLikeRepository;
import todoktodok.backend.discussion.domain.repository.DiscussionRepository;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.reply.domain.repository.ReplyRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class DiscussionQueryService {

    private static final int MIN_HOT_DISCUSSION_COUNT = 1;
    private static final int MIN_HOT_DISCUSSION_PERIOD = 0;
    private static final int MAX_HOT_DISCUSSION_PERIOD = 365;

    private final DiscussionRepository discussionRepository;
    private final DiscussionLikeRepository discussionLikeRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    public DiscussionResponse getDiscussion(
            final Long memberId,
            final Long discussionId
    ) {
        final Member member = findMember(memberId);
        final Discussion discussion = findDiscussion(discussionId);

        final int likeCount = Math.toIntExact(discussionLikeRepository.findLikeCountsByDiscussionId(discussionId));
        final int commentCount = Math.toIntExact(
                commentRepository.countCommentsByDiscussionId(discussionId)
                        + replyRepository.countRepliesByDiscussionId(discussionId)
        );
        final boolean isLiked = discussionLikeRepository.existsByMemberAndDiscussion(member, discussion);

        return new DiscussionResponse(
                discussion,
                likeCount,
                commentCount,
                isLiked
        );
    }

    public List<DiscussionResponse> getDiscussionsByKeywordAndType(
            final Long memberId,
            final String keyword,
            final DiscussionFilterType type
    ) {
        final Member member = findMember(memberId);

        if (isKeywordBlank(keyword)) {
            return getDiscussionsByType(type, member);
        }

        if (type.isTypeMine()) {
            return getMyDiscussionsByKeyword(keyword, member);
        }

        return getDiscussionsByKeyword(keyword, member);
    }

    public List<DiscussionResponse> getHotDiscussions(
            final Long memberId,
            final int period,
            final int count
    ) {
        validateHotDiscussionPeriod(period);
        validateHotDiscussionCount(count);

        final Member member = findMember(memberId);
        final LocalDateTime sinceDate = LocalDate.now().minusDays(period).atStartOfDay();
        final List<Discussion> discussions = discussionRepository.findAll();

        if (discussions.isEmpty()) {
            return Collections.emptyList();
        }

        final List<Long> discussionIds = discussions.stream()
                .map(Discussion::getId)
                .toList();

        final List<DiscussionLikeCountDto> likeSinceCounts = discussionLikeRepository.findLikeCountsByDiscussionIdsSinceDate(
                discussionIds, sinceDate);
        final List<DiscussionCommentCountDto> commentSinceCounts = commentRepository.findCommentCountsByDiscussionIdsSinceDate(
                discussionIds, sinceDate);

        final Map<Long, Integer> likesByDiscussionId = getLikeCountsByDiscussionId(likeSinceCounts);
        final Map<Long, Integer> commentsByDiscussionId = getCommentCountsByDiscussionId(commentSinceCounts);

        final List<Discussion> hotDiscussions = findHotDiscussions(count, likesByDiscussionId, commentsByDiscussionId, discussions);

        final List<Long> likedDiscussionIds = getLikedDiscussionIdsFromHot(hotDiscussions, member);

        return makeResponsesFrom(hotDiscussions, likesByDiscussionId, commentsByDiscussionId, likedDiscussionIds);
    }

    private boolean isKeywordBlank(final String keyword) {
        return keyword == null || keyword.isBlank();
    }

    private Discussion findDiscussion(final Long discussionId) {
        return discussionRepository.findById(discussionId)
                .orElseThrow(() -> new NoSuchElementException(
                                String.format("해당 토론방을 찾을 수 없습니다: discussionId= %s", discussionId)
                        )
                );
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException(
                                String.format("해당 회원을 찾을 수 없습니다: memberId= %s", memberId)
                        )
                );
    }

    private List<DiscussionResponse> getDiscussionsByType(
            final DiscussionFilterType type,
            final Member member
    ) {
        if (type.isTypeMine()) {
            return getMyDiscussions(member);
        }
        return getAllDiscussions(member);
    }

    private List<DiscussionResponse> getAllDiscussions(final Member member) {
        final List<Discussion> discussions = discussionRepository.findAll();
        return getDiscussionsResponses(discussions, member);
    }

    private List<DiscussionResponse> getMyDiscussions(final Member member) {
        final List<Discussion> discussions = discussionRepository.findDiscussionsByMember(member);

        return getDiscussionsResponses(discussions, member);
    }

    private List<DiscussionResponse> getMyDiscussionsByKeyword(
            final String keyword,
            final Member member
    ) {
        final List<Discussion> discussions = discussionRepository.searchByKeywordAndMember(keyword, member).stream()
                .filter(discussion -> discussion.isOwnedBy(member))
                .toList();

        return getDiscussionsResponses(discussions, member);
    }

    private List<DiscussionResponse> getDiscussionsByKeyword(
            final String keyword,
            final Member member
    ) {
        final List<Discussion> discussions = discussionRepository.searchByKeyword(keyword);

        return getDiscussionsResponses(discussions, member);
    }

    private List<DiscussionResponse> getDiscussionsResponses(
            final List<Discussion> discussions,
            final Member member
    ) {
        final List<Long> discussionIds = discussions.stream()
                .map(Discussion::getId)
                .toList();

        final List<DiscussionLikeCountDto> likeCounts = discussionLikeRepository.findLikeCountsByDiscussionIds(
                discussionIds);
        final List<DiscussionCommentCountDto> commentCounts = commentRepository.findCommentCountsByDiscussionIds(
                discussionIds);
        final Map<Long, Integer> likesByDiscussionId = getLikeCountsByDiscussionId(likeCounts);
        final Map<Long, Integer> commentsByDiscussionId = getCommentCountsByDiscussionId(commentCounts);
        final List<Long> likedDiscussionIds = discussionLikeRepository.findLikedDiscussionIdsByMember(member, discussionIds);

        return makeResponsesFrom(discussions, likesByDiscussionId, commentsByDiscussionId, likedDiscussionIds);
    }

    private List<DiscussionResponse> makeResponsesFrom(
            final List<Discussion> discussions,
            final Map<Long, Integer> likeCountsByDiscussionId,
            final Map<Long, Integer> commentCountsByDiscussionId,
            final List<Long> likedDiscussionIds
    ) {
        return discussions.stream()
                .map(discussion -> {
                    final long discussionId = discussion.getId();
                    final int likeCount = likeCountsByDiscussionId.getOrDefault(discussionId, 0);
                    final int commentCount = commentCountsByDiscussionId.getOrDefault(discussionId, 0);
                    final boolean isLikedByMe = likedDiscussionIds.contains(discussionId);
                    return new DiscussionResponse(discussion, likeCount, commentCount, isLikedByMe);
                })
                .toList();
    }


    private Map<Long, Integer> getCommentCountsByDiscussionId(final List<DiscussionCommentCountDto> commentCounts) {
        return commentCounts.stream()
                .collect(Collectors.toMap(
                        DiscussionCommentCountDto::discussionId,
                        dto -> dto.commentCount() + dto.replyCount()
                ));
    }

    private Map<Long, Integer> getLikeCountsByDiscussionId(final List<DiscussionLikeCountDto> likeCounts) {
        return likeCounts.stream()
                .collect(Collectors.toMap(
                        DiscussionLikeCountDto::discussionId,
                        DiscussionLikeCountDto::likeCount
                ));
    }

    private static void validateHotDiscussionCount(final int count) {
        if (count < MIN_HOT_DISCUSSION_COUNT) {
            throw new IllegalArgumentException(String.format("유효하지 않은 개수입니다. 양수의 개수를 조회해주세요: count = %d", count));
        }
    }

    private static void validateHotDiscussionPeriod(final int period) {
        if (period < MIN_HOT_DISCUSSION_PERIOD || period > MAX_HOT_DISCUSSION_PERIOD) {
            throw new IllegalArgumentException(String.format("유효하지 않은 기간 값입니다. 0일 ~ 365일 이내로 조회해주세요: period = %d", period));
        }
    }

    private static List<Discussion> findHotDiscussions(
            final int count,
            final Map<Long, Integer> likesByDiscussionId,
            final Map<Long, Integer> commentsByDiscussionId,
            final List<Discussion> discussions
    ) {
        final ToIntFunction<Discussion> totalCountByDiscussion =
                discussion ->
                        likesByDiscussionId.getOrDefault(discussion.getId(), 0)
                                + commentsByDiscussionId.getOrDefault(discussion.getId(), 0);

        return discussions.stream()
                .sorted(Comparator
                        .comparingInt(totalCountByDiscussion)
                        .reversed()
                        .thenComparing(Discussion::getId, Comparator.reverseOrder())
                )
                .limit(count)
                .toList();
    }

    private List<Long> getLikedDiscussionIdsFromHot(
            final List<Discussion> hotDiscussions,
            final Member member
    ) {
        final List<Long> hotDiscussionIds = hotDiscussions.stream().map(Discussion::getId).toList();
        return discussionLikeRepository.findLikedDiscussionIdsByMember(member, hotDiscussionIds);
    }
}
