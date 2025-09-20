package todoktodok.backend.discussion.application.service.query;

import jakarta.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.comment.domain.repository.CommentRepository;
import todoktodok.backend.discussion.application.dto.ActiveDiscussionCursor;
import todoktodok.backend.discussion.application.dto.response.ActiveDiscussionPageResponse;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.discussion.application.dto.response.LatestDiscussionPageResponse;
import todoktodok.backend.discussion.application.dto.response.PageInfo;
import todoktodok.backend.discussion.domain.Discussion;
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
    private static final int MAX_PAGE_SIZE = 50;
    private static final int MIN_PAGE_SIZE = 1;

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
        final boolean isLikedByMe = discussionLikeRepository.existsByMemberAndDiscussion(member, discussion);

        return new DiscussionResponse(
                discussion,
                likeCount,
                commentCount,
                isLikedByMe
        );
    }

    public LatestDiscussionPageResponse getDiscussions(
            final Long memberId,
            final int size,
            @Nullable final String cursor
    ) {
        validatePageSize(size);
        final Member member = findMember(memberId);

        final Slice<Discussion> slicedDiscussions = sliceDiscussionsBy(cursor, size);

        final List<Discussion> discussions = slicedDiscussions.getContent();
        final boolean hasNextPage = slicedDiscussions.hasNext();
        final String nextCursor = findNextCursor(hasNextPage, discussions);

        return new LatestDiscussionPageResponse(
                getDiscussionsResponses(discussions, member),
                new PageInfo(hasNextPage, nextCursor)
        );
    }

    public List<DiscussionResponse> getDiscussionsByKeyword(
            final Long memberId,
            final String keyword
    ) {
        validateKeywordNotBlank(keyword);

        final Member member = findMember(memberId);
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
        final Map<Long, Integer> commentsByDiscussionId = getTotalCommentCountsByDiscussionId(commentSinceCounts);

        final List<Discussion> hotDiscussions = findHotDiscussions(count, likesByDiscussionId, commentsByDiscussionId,
                discussions);

        final List<Long> likedDiscussionIds = getLikedDiscussionIdsFromHot(hotDiscussions, member);

        return makeResponsesFrom(hotDiscussions, likesByDiscussionId, commentsByDiscussionId, likedDiscussionIds);
    }

    public ActiveDiscussionPageResponse getActiveDiscussions(
            final Long memberId,
            final int period,
            final int requestedSize,
            @Nullable final String cursor
    ) {
        validatePageSize(requestedSize);
        final Member member = findMember(memberId);
        final LocalDateTime periodStart = LocalDateTime.now().minusDays(period);
        final String normalizedCursor = processBlankCursor(cursor);

        final ActiveDiscussionCursor activeDiscussionCursor = Optional.ofNullable(normalizedCursor)
                .map(ActiveDiscussionCursor::fromEncoded)
                .orElse(ActiveDiscussionCursor.empty());
        final Pageable pageable = Pageable.ofSize(requestedSize + 1);
        final List<Discussion> discussions = discussionRepository.findActiveDiscussionsByCursor(
                periodStart,
                activeDiscussionCursor.lastDiscussionLatestCommentId(),
                pageable
        );

        if (discussions.isEmpty()) {
            return new ActiveDiscussionPageResponse(Collections.emptyList(), new PageInfo(false, null));
        }

        final boolean hasNext = discussions.size() > requestedSize;
        if (hasNext) {
            discussions.removeLast();
        }

        final Discussion lastDiscussion = hasNext ? discussions.getLast() : null;
        final Long latestCommentIdByDiscussion = commentRepository.findLatestCommentIdByDiscussion(lastDiscussion,
                        periodStart)
                .orElse(null);

        final List<DiscussionResponse> discussionResponses = getDiscussionsResponses(discussions, member);
        final String nextCursor = getNextCursor(hasNext, latestCommentIdByDiscussion);

        return new ActiveDiscussionPageResponse(
                discussionResponses,
                new PageInfo(hasNext, nextCursor)
        );
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

    private Slice<Discussion> sliceDiscussionsBy(
            final String cursor,
            final int size
    ) {
        final Pageable pageable = PageRequest.of(0, size, Sort.Direction.DESC, "id");

        if (cursor == null || cursor.isBlank()) {
            return discussionRepository.findAllBy(pageable);
        }

        final Long cursorId = decodeCursor(cursor);
        return discussionRepository.findByIdLessThan(cursorId, pageable);
    }

    private String processBlankCursor(String cursor) {
        if (cursor == null || cursor.isBlank()) {
            return null;
        }
        return cursor;
    }

    private String findNextCursor(
            final boolean hasNextPage,
            final List<Discussion> discussions
    ) {
        if (!hasNextPage || discussions.isEmpty()) {
            return null;
        }
        return encodeCursorId(discussions.getLast().getId());
    }

    private Long decodeCursor(final String cursor) {
        try {
            if (cursor == null || cursor.isBlank()) {
                return null;
            }

            final String decoded = new String(Base64.getUrlDecoder().decode(cursor));
            return Long.valueOf(decoded);
        } catch (final Exception e) {
            throw new IllegalArgumentException(String.format("Base64로 디코드할 수 없는 cursor 값입니다: cursor = %s", cursor));
        }
    }

    private String encodeCursorId(final Long id) {
        return Base64.getUrlEncoder().encodeToString(id.toString().getBytes());
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

        if (discussionIds.isEmpty()) {
            return List.of();
        }

        final List<DiscussionLikeCountDto> likeCounts = discussionLikeRepository.findLikeCountsByDiscussionIds(
                discussionIds);
        final List<DiscussionCommentCountDto> commentCounts = commentRepository.findCommentCountsByDiscussionIds(
                discussionIds);
        final Map<Long, Integer> likesByDiscussionId = getLikeCountsByDiscussionId(likeCounts);
        final Map<Long, Integer> commentsByDiscussionId = getTotalCommentCountsByDiscussionId(commentCounts);
        final List<Long> likedDiscussionIds = discussionLikeRepository.findLikedDiscussionIdsByMember(member,
                discussionIds);

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


    private Map<Long, Integer> getTotalCommentCountsByDiscussionId(
            final List<DiscussionCommentCountDto> commentCounts) {
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
            throw new IllegalArgumentException(
                    String.format("유효하지 않은 기간 값입니다. 0일 ~ 365일 이내로 조회해주세요: period = %d", period));
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

    private String getNextCursor(
            final boolean hasNext,
            final Long lastDiscussionLatestCommentId
    ) {
        if (!hasNext || lastDiscussionLatestCommentId == null) {
            return null;
        }

        final ActiveDiscussionCursor activeDiscussionCursor = new ActiveDiscussionCursor(lastDiscussionLatestCommentId);
        return activeDiscussionCursor.toEncoded();
    }

    private void validateKeywordNotBlank(final String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException(
                    String.format("검색 키워드를 입력해야 합니다: keyword= %s", keyword)
            );
        }
    }

    private void validatePageSize(final int size) {
        if (size < MIN_PAGE_SIZE || size > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException(
                    String.format("유효하지 않은 페이지 사이즈입니다. 1 이상 50 이하의 페이징을 시도해주세요: size = %d", size));
        }
    }
}
