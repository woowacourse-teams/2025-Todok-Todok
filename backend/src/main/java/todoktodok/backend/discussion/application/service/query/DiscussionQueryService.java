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
import todoktodok.backend.discussion.application.dto.DiscussionCursor;
import todoktodok.backend.discussion.application.dto.response.ActiveDiscussionPageResponse;
import todoktodok.backend.discussion.application.dto.response.ActiveDiscussionResponse;
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
    private static final int MIN_DISCUSSION_PERIOD = 0;
    private static final int MAX_DISCUSSION_PERIOD = 7;
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

        final Slice<Long> discussionIdsSlice = sliceDiscussionsBy(cursor, size);

        final List<Long> discussionIds = discussionIdsSlice.getContent();
        final boolean hasNextPage = discussionIdsSlice.hasNext();
        final String nextCursor = findNextCursor(hasNextPage, discussionIds);

        return new LatestDiscussionPageResponse(
                getDiscussionsResponses(discussionIds, member),
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
        validateDiscussionPeriod(period);
        validateHotDiscussionCount(count);

        final Member member = findMember(memberId);
        final LocalDateTime sinceDate = LocalDate.now().minusDays(period).atStartOfDay();
        final List<Long> discussionIds = discussionRepository.findAllIdsBy();

        if (discussionIds.isEmpty()) {
            return Collections.emptyList();
        }

        final List<DiscussionLikeCountDto> likeSinceCounts = discussionLikeRepository.findLikeCountsByDiscussionIdsSinceDate(
                discussionIds, sinceDate);
        final List<DiscussionCommentCountDto> commentSinceCounts = commentRepository.findCommentCountsByDiscussionIdsSinceDate(
                discussionIds, sinceDate);

        final Map<Long, Integer> likesByDiscussionId = getLikeCountsByDiscussionId(likeSinceCounts);
        final Map<Long, Integer> commentsByDiscussionId = getTotalCommentCountsByDiscussionId(commentSinceCounts);

        final List<Long> hotDiscussionIds = findHotDiscussions(count, likesByDiscussionId, commentsByDiscussionId, discussionIds);

        final List<Long> likedDiscussionIds = getLikedDiscussionIdsFromHot(hotDiscussionIds, member);

        return makeResponsesFrom(hotDiscussionIds, likesByDiscussionId, commentsByDiscussionId, likedDiscussionIds);
    }

    public ActiveDiscussionPageResponse getActiveDiscussions(
            final Long memberId,
            final int period,
            final int size,
            @Nullable final String cursor
    ) {
        validateDiscussionPeriod(period);
        validatePageSize(size);

        final Member member = findMember(memberId);
        final LocalDateTime periodStart = LocalDateTime.now().minusDays(period);

        final DiscussionCursor discussionCursor = Optional.ofNullable(cursor)
                .map(DiscussionCursor::fromEncoded)
                .orElse(DiscussionCursor.empty());

        final Pageable pageable = Pageable.ofSize(size + 1);

        final List<ActiveDiscussionResponse> discussions = discussionRepository.findActiveDiscussionsByCursor(
                member,
                periodStart,
                discussionCursor.lastCommentedAt(),
                discussionCursor.cursorId(),
                pageable
        );

        if (discussions.isEmpty()) {
            return new ActiveDiscussionPageResponse(Collections.emptyList(), new PageInfo(false, null));
        }

        final boolean hasNext = discussions.size() > size;
        if (hasNext) {
            discussions.removeLast();
        }

        final ActiveDiscussionResponse last = hasNext ? discussions.getLast() : null;
        final String nextCursor = getNextCursor(hasNext, last, discussionCursor);

        return new ActiveDiscussionPageResponse(
                discussions,
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
        return memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new NoSuchElementException(
                                String.format("해당 회원을 찾을 수 없습니다: memberId= %s", memberId)
                        )
                );
    }

    private Slice<Long> sliceDiscussionsBy(
            final String cursor,
            final int size
    ) {
        Pageable pageable = PageRequest.of(0, size, Sort.Direction.DESC, "id");

        if (cursor == null || cursor.isBlank()) {
            return discussionRepository.findAllIdsBy(pageable);
        }

        final Long cursorId = decodeCursor(cursor);
        return discussionRepository.findIdsLessThan(cursorId, pageable);
    }

    private String findNextCursor(
            final boolean hasNextPage,
            final List<Long> discussionIds
    ) {
        if (!hasNextPage || discussionIds.isEmpty()) {
            return null;
        }
        return encodeCursorId(discussionIds.getLast());
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
        final List<Long> discussionIds = discussionRepository.searchIdsByKeyword(keyword);

        return getDiscussionsResponses(discussionIds, member);
    }

    private List<DiscussionResponse> getDiscussionsResponses(
            final List<Long> discussionIds,
            final Member member
    ) {
        if (discussionIds.isEmpty()) {
            return List.of();
        }

        final List<DiscussionLikeCountDto> likeCounts = discussionLikeRepository.findLikeCountsByDiscussionIds(discussionIds);
        final List<DiscussionCommentCountDto> commentCounts = commentRepository.findCommentCountsByDiscussionIds(discussionIds);

        final Map<Long, Integer> likesByDiscussionId = getLikeCountsByDiscussionId(likeCounts);
        final Map<Long, Integer> commentsByDiscussionId = getTotalCommentCountsByDiscussionId(commentCounts);

        final List<Long> likedDiscussionIds = discussionLikeRepository.findLikedDiscussionIdsByMember(member, discussionIds);

        return makeResponsesFrom(discussionIds, likesByDiscussionId, commentsByDiscussionId, likedDiscussionIds);
    }

    private List<DiscussionResponse> makeResponsesFrom(
            final List<Long> discussionIds,
            final Map<Long, Integer> likeCountsByDiscussionId,
            final Map<Long, Integer> commentCountsByDiscussionId,
            final List<Long> likedDiscussionIds
    ) {
        final Map<Long, Discussion> discussions = discussionRepository.findDiscussionsInIds(discussionIds).stream()
                .collect(Collectors.toMap(Discussion::getId, discussion -> discussion));

        return discussionIds.stream()
                .map(discussionId -> {
                    final Discussion discussion = discussions.get(discussionId);
                    final int likeCount = likeCountsByDiscussionId.getOrDefault(discussionId, 0);
                    final int commentCount = commentCountsByDiscussionId.getOrDefault(discussionId, 0);
                    final boolean isLikedByMe = likedDiscussionIds.contains(discussionId);
                    return new DiscussionResponse(discussion, likeCount, commentCount, isLikedByMe);
                })
                .toList();
    }


    private Map<Long, Integer> getTotalCommentCountsByDiscussionId(final List<DiscussionCommentCountDto> commentCounts) {
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

    private static void validateDiscussionPeriod(final int period) {
        if (period < MIN_DISCUSSION_PERIOD || period > MAX_DISCUSSION_PERIOD) {
            throw new IllegalArgumentException(String.format("유효하지 않은 기간 값입니다. 0일 ~ 7일 이내로 조회해주세요: period = %d", period));
        }
    }

    private static List<Long> findHotDiscussions(
            final int count,
            final Map<Long, Integer> likesByDiscussionId,
            final Map<Long, Integer> commentsByDiscussionId,
            final List<Long> discussionIds
    ) {
        final ToIntFunction<Long> totalCountByDiscussion =
                discussionId ->
                        likesByDiscussionId.getOrDefault(discussionId, 0)
                                + commentsByDiscussionId.getOrDefault(discussionId, 0);

        return discussionIds.stream()
                .sorted(Comparator
                        .comparingInt(totalCountByDiscussion)
                        .reversed()
                        .thenComparing(discussionId -> discussionId, Comparator.reverseOrder())
                )
                .limit(count)
                .toList();
    }

    private List<Long> getLikedDiscussionIdsFromHot(
            final List<Long> hotDiscussionIds,
            final Member member
    ) {
        return discussionLikeRepository.findLikedDiscussionIdsByMember(member, hotDiscussionIds);
    }

    private String getNextCursor(
            final boolean hasNext,
            final ActiveDiscussionResponse last,
            final DiscussionCursor discussionCursor
    ) {
        if (!hasNext || last == null) {
            return null;
        }
        return discussionCursor.toEncoded(last.lastCommentedAt(), last.discussionId());
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
