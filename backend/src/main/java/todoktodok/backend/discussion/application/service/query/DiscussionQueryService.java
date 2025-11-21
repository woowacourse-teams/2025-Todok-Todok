package todoktodok.backend.discussion.application.service.query;

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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.comment.domain.repository.CommentRepository;
import todoktodok.backend.discussion.application.dto.ActiveDiscussionCursor;
import todoktodok.backend.discussion.application.dto.response.ActiveDiscussionPageResponse;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.discussion.application.dto.response.LatestDiscussionPageResponse;
import todoktodok.backend.discussion.application.dto.response.PageInfo;
import todoktodok.backend.discussion.application.service.event.DiscussionViewEvent;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.repository.DiscussionLikeRepository;
import todoktodok.backend.discussion.domain.repository.DiscussionRepository;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;

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
    private final ApplicationEventPublisher eventPublisher;

    public DiscussionResponse getDiscussion(
            final Long memberId,
            final Long discussionId
    ) {
        final Member member = findMember(memberId);
        final Discussion discussion = findDiscussion(discussionId);
        final DiscussionViewEvent discussionViewEvent = new DiscussionViewEvent(member.getId(), discussion.getId());

        final DiscussionLikeSummaryDto likeSummary = discussionLikeRepository.findLikeSummaryByDiscussionId(member,
                discussionId);
        final DiscussionCommentCountDto commentSummary = commentRepository.findCommentCountByDiscussionId(discussionId);
        final int commentCount = commentSummary.commentCount() + commentSummary.replyCount();

        eventPublisher.publishEvent(discussionViewEvent);

        return new DiscussionResponse(discussion, likeSummary.likeCount(), commentCount, likeSummary.isLikedByMe());
    }

    public LatestDiscussionPageResponse getDiscussions(
            final Long memberId,
            final int size,
            final String cursor
    ) {
        validatePageSize(size);
        final Member member = findMember(memberId);

        final Slice<Discussion> discussionSlice = sliceDiscussionsBy(cursor, size);

        return createPageResponse(discussionSlice, member);
    }

    public List<DiscussionResponse> getDiscussionsByKeyword(
            final Long memberId,
            final String keyword
    ) {
        validateKeywordNotBlank(keyword);

        final Member member = findMember(memberId);
        return getDiscussionsByKeyword(keyword, member);
    }

    public LatestDiscussionPageResponse getDiscussionsByBook(
            final Long memberId,
            final Long bookId,
            final int size,
            final String cursor
    ) {
        validatePageSize(size);
        final Member member = findMember(memberId);

        final Pageable pageable = PageRequest.of(0, size, Direction.DESC, "id");
        final Slice<Discussion> discussionSlice = sliceDiscussionsByBook(bookId, cursor, pageable);

        return createPageResponse(discussionSlice, member);
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
        final List<Long> discussionIds = discussionRepository.findAllIds();

        if (discussionIds.isEmpty()) {
            return Collections.emptyList();
        }

        final List<DiscussionLikeSummaryDto> likeSinceCounts = discussionLikeRepository.findLikeSummariesByDiscussionIdsSinceDate(
                member, discussionIds, sinceDate);
        final List<DiscussionCommentCountDto> commentSinceCounts = commentRepository.findCommentCountsByDiscussionIdsSinceDate(
                discussionIds, sinceDate);

        final Map<Long, LikeCountAndIsLikedByMeDto> likesByDiscussionId = mapLikeSummariesByDiscussionId(
                likeSinceCounts);
        final Map<Long, Integer> commentsByDiscussionId = mapTotalCommentCountsByDiscussionId(commentSinceCounts);

        final List<Discussion> hotDiscussions = findHotDiscussions(count, likesByDiscussionId, commentsByDiscussionId,
                discussionIds);

        return getDiscussionsResponses(hotDiscussions, member);
    }

    public ActiveDiscussionPageResponse getActiveDiscussions(
            final Long memberId,
            final int period,
            final int requestedSize,
            final String cursor
    ) {
        validateDiscussionPeriod(period);
        validatePageSize(requestedSize);

        final Member member = findMember(memberId);
        final LocalDateTime periodStart = LocalDateTime.now().minusDays(period);
        final String normalizedCursor = processBlankCursor(cursor);

        final ActiveDiscussionCursor activeDiscussionCursor = Optional.ofNullable(normalizedCursor)
                .map(ActiveDiscussionCursor::fromEncoded)
                .orElse(ActiveDiscussionCursor.empty());
        final Pageable pageable = Pageable.ofSize(requestedSize + 1);
        final List<Discussion> activeDiscussions = discussionRepository.findActiveDiscussionsByCursor(
                periodStart,
                activeDiscussionCursor.lastDiscussionLatestCommentId(),
                pageable
        );

        if (activeDiscussions.isEmpty()) {
            return new ActiveDiscussionPageResponse(Collections.emptyList(), new PageInfo(false, null));
        }

        final boolean hasNext = activeDiscussions.size() > requestedSize;
        if (hasNext) {
            activeDiscussions.removeLast();
        }

        final Discussion lastDiscussion = getLastDiscussion(activeDiscussions, hasNext);
        final Long latestCommentIdByDiscussion = commentRepository.findLatestCommentIdByDiscussion(lastDiscussion,
                        periodStart)
                .orElse(null);

        final List<DiscussionResponse> discussionResponses = getDiscussionsResponses(activeDiscussions, member);
        final String nextCursor = getNextCursor(hasNext, latestCommentIdByDiscussion);

        return new ActiveDiscussionPageResponse(
                discussionResponses,
                new PageInfo(hasNext, nextCursor)
        );
    }

    public List<DiscussionResponse> getLikedDiscussionsByMe(final Long memberId) {
        final Member member = findMember(memberId);
        final List<Discussion> likedDiscussions = discussionLikeRepository.findLikedDiscussionIdsByMember(member);

        return getDiscussionsResponses(likedDiscussions, member);
    }

    private Discussion getLastDiscussion(
            final List<Discussion> discussions,
            final boolean hasNext
    ) {
        final Long lastDid = discussions.getLast().getId();
        if (hasNext) {
            return discussionRepository.findById(lastDid).get();
        }
        return null;
    }

    private Discussion findDiscussion(final Long discussionId) {
        return discussionRepository.findByIdWithMemberAndBook(discussionId)
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

    private Slice<Discussion> sliceDiscussionsBy(
            final String cursor,
            final int size
    ) {
        final Pageable pageable = PageRequest.of(0, size, Sort.Direction.DESC, "id");

        if (cursor == null || cursor.isBlank()) {
            return discussionRepository.findAllBy(pageable);
        }

        final Long cursorId = decodeCursor(cursor);
        return discussionRepository.findDiscussionsLessThan(cursorId, pageable);
    }

    private String processBlankCursor(final String cursor) {
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
        final String keywordWithPrefix = String.format("+%s*", keyword);
        final List<Long> discussionIds = discussionRepository.searchIdsByKeyword(keywordWithPrefix);
        final List<Discussion> discussions = discussionRepository.findDiscussionsInIds(discussionIds);

        return getDiscussionsResponses(discussions, member);
    }

    private List<DiscussionResponse> getDiscussionsResponses(
            final List<Discussion> discussions,
            final Member member
    ) {
        if (discussions.isEmpty()) {
            return List.of();
        }

        final List<Long> discussionIds = discussions.stream()
                .map(Discussion::getId)
                .toList();

        final List<DiscussionLikeSummaryDto> likeSummaries = discussionLikeRepository.findLikeSummaryByDiscussionIds(
                member, discussionIds);
        final List<DiscussionCommentCountDto> commentCounts = commentRepository.findCommentCountsByDiscussionIds(
                discussionIds);

        final Map<Long, LikeCountAndIsLikedByMeDto> likesByDiscussionId = mapLikeSummariesByDiscussionId(likeSummaries);
        final Map<Long, Integer> commentsByDiscussionId = mapTotalCommentCountsByDiscussionId(commentCounts);

        return makeResponsesFrom(discussions, likesByDiscussionId, commentsByDiscussionId);
    }

    private List<DiscussionResponse> makeResponsesFrom(
            final List<Discussion> discussions,
            final Map<Long, LikeCountAndIsLikedByMeDto> likeSummaryByDiscussionId,
            final Map<Long, Integer> commentCountsByDiscussionId
    ) {
        return discussions.stream()
                .map(discussion -> {
                    final Long discussionId = discussion.getId();
                    final int likeCount = likeSummaryByDiscussionId.get(discussionId).likeCount();
                    final int commentCount = commentCountsByDiscussionId.getOrDefault(discussionId, 0);
                    final boolean isLikedByMe = likeSummaryByDiscussionId.get(discussionId).isLikedByMe();
                    return new DiscussionResponse(discussion, likeCount, commentCount, isLikedByMe);
                })
                .toList();
    }

    private Map<Long, Integer> mapTotalCommentCountsByDiscussionId(
            final List<DiscussionCommentCountDto> commentCounts) {
        return commentCounts.stream()
                .collect(Collectors.toMap(
                        DiscussionCommentCountDto::discussionId,
                        dto -> dto.commentCount() + dto.replyCount()
                ));
    }

    private Map<Long, LikeCountAndIsLikedByMeDto> mapLikeSummariesByDiscussionId(
            final List<DiscussionLikeSummaryDto> likeCounts) {
        return likeCounts.stream()
                .collect(Collectors.toMap(
                        DiscussionLikeSummaryDto::discussionId,
                        discussionLikeSummaryDto ->
                                new LikeCountAndIsLikedByMeDto(
                                        discussionLikeSummaryDto.likeCount(),
                                        discussionLikeSummaryDto.isLikedByMe()
                                )
                ));
    }

    private void validateHotDiscussionCount(final int count) {
        if (count < MIN_HOT_DISCUSSION_COUNT) {
            throw new IllegalArgumentException(String.format("유효하지 않은 개수입니다. 양수의 개수를 조회해주세요: count = %d", count));
        }
    }

    private void validateDiscussionPeriod(final int period) {
        if (period < MIN_DISCUSSION_PERIOD || period > MAX_DISCUSSION_PERIOD) {
            throw new IllegalArgumentException(
                    String.format("유효하지 않은 기간 값입니다. 0일 ~ 7일 이내로 조회해주세요: period = %d", period));
        }
    }

    private List<Discussion> findHotDiscussions(
            final int count,
            final Map<Long, LikeCountAndIsLikedByMeDto> likesByDiscussionId,
            final Map<Long, Integer> commentsByDiscussionId,
            final List<Long> discussionIds
    ) {
        final ToIntFunction<Long> totalCountByDiscussion =
                discussionId ->
                        likesByDiscussionId.get(discussionId).likeCount()
                                + commentsByDiscussionId.getOrDefault(discussionId, 0);

        final List<Long> hotDiscussionIds = discussionIds.stream()
                .sorted(Comparator
                        .comparingInt(totalCountByDiscussion)
                        .reversed()
                        .thenComparing(discussionId -> discussionId, Comparator.reverseOrder())
                )
                .limit(count)
                .toList();

        final Map<Long, Discussion> hotDiscussions = discussionRepository.findDiscussionsInIds(hotDiscussionIds).stream()
                .collect(Collectors.toMap(Discussion::getId, discussion -> discussion));

        return hotDiscussionIds.stream().map(hotDiscussions::get).toList();
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

    private Slice<Discussion> sliceDiscussionsByBook(
            final Long bookId,
            final String cursor,
            final Pageable pageable
    ) {
        if (cursor == null || cursor.isBlank()) {
            return discussionRepository.findDiscussionsByBookId(bookId, pageable);
        }

        final Long cursorId = decodeCursor(cursor);
        return discussionRepository.findDiscussionsByBookIdLessThan(bookId, cursorId, pageable);
    }

    private LatestDiscussionPageResponse createPageResponse(
            final Slice<Discussion> discussionSlice,
            final Member member
    ) {
        final List<Discussion> discussions = discussionSlice.getContent();
        final boolean hasNextPage = discussionSlice.hasNext();
        final String nextCursor = findNextCursor(hasNextPage, discussions);

        return new LatestDiscussionPageResponse(
                getDiscussionsResponses(discussions, member),
                new PageInfo(hasNextPage, nextCursor)
        );
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
