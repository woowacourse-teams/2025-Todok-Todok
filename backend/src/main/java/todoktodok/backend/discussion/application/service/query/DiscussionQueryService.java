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
import todoktodok.backend.discussion.domain.DiscussionMemberView;
import todoktodok.backend.discussion.domain.repository.DiscussionLikeRepository;
import todoktodok.backend.discussion.domain.repository.DiscussionMemberViewRepository;
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
    private static final int VIEW_THRESHOLD = 10;

    private final DiscussionRepository discussionRepository;
    private final DiscussionLikeRepository discussionLikeRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final DiscussionMemberViewRepository discussionMemberViewRepository;

    @Transactional
    public DiscussionResponse getDiscussion(
            final Long memberId,
            final Long discussionId
    ) {
        final Member member = findMember(memberId);
        final Discussion discussion = findDiscussion(discussionId);

        final DiscussionLikeSummaryDto likeSummary = discussionLikeRepository.findLikeSummaryByDiscussionId(member, discussionId);
        final DiscussionCommentCountDto commentSummary = commentRepository.findCommentCountByDiscussionId(discussionId);
        final int commentCount = commentSummary.commentCount() + commentSummary.replyCount();

        Optional<DiscussionMemberView> discussionMemberView = discussionMemberViewRepository.findByMemberAndDiscussion(
                member, discussion);

        if (discussionMemberView.isEmpty()) {
            discussionMemberView = Optional.ofNullable(DiscussionMemberView.builder()
                    .discussion(discussion)
                    .member(member)
                    .build());
            discussionMemberViewRepository.save(discussionMemberView.get());
            discussion.updateViewCount();

            return new DiscussionResponse(discussion, likeSummary.likeCount(), commentCount, likeSummary.isLikedByMe());
        }

        if (discussionMemberView.get().isModifiedDatePassedFrom(VIEW_THRESHOLD)) {
            discussion.updateViewCount();
        }

        return new DiscussionResponse(discussion, likeSummary.likeCount(), commentCount, likeSummary.isLikedByMe());
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
        final List<Long> discussionIds = discussionRepository.findAllIds();

        if (discussionIds.isEmpty()) {
            return Collections.emptyList();
        }

        final List<DiscussionLikeSummaryDto> likeSinceCounts = discussionLikeRepository.findLikeSummariesByDiscussionIdsSinceDate(
                member, discussionIds, sinceDate);
        final List<DiscussionCommentCountDto> commentSinceCounts = commentRepository.findCommentCountsByDiscussionIdsSinceDate(
                discussionIds, sinceDate);

        final Map<Long, LikeCountAndIsLikedByMeDto> likesByDiscussionId = mapLikeSummariesByDiscussionId(likeSinceCounts);
        final Map<Long, Integer> commentsByDiscussionId = mapTotalCommentCountsByDiscussionId(commentSinceCounts);

        final List<Long> hotDiscussionIds = findHotDiscussions(count, likesByDiscussionId, commentsByDiscussionId, discussionIds);

        return getDiscussionsResponses(hotDiscussionIds, member);
    }

    public ActiveDiscussionPageResponse getActiveDiscussions(
            final Long memberId,
            final int period,
            final int requestedSize,
            @Nullable final String cursor
    ) {
        validateDiscussionPeriod(period);
        validatePageSize(requestedSize);

        validatePageSize(requestedSize);
        final Member member = findMember(memberId);
        final LocalDateTime periodStart = LocalDateTime.now().minusDays(period);
        final String normalizedCursor = processBlankCursor(cursor);

        final ActiveDiscussionCursor activeDiscussionCursor = Optional.ofNullable(normalizedCursor)
                .map(ActiveDiscussionCursor::fromEncoded)
                .orElse(ActiveDiscussionCursor.empty());
        final Pageable pageable = Pageable.ofSize(requestedSize + 1);
        final List<Long> activeDiscussionIds = discussionRepository.findActiveDiscussionsByCursor(
                periodStart,
                activeDiscussionCursor.lastDiscussionLatestCommentId(),
                pageable
        );

        if (activeDiscussionIds.isEmpty()) {
            return new ActiveDiscussionPageResponse(Collections.emptyList(), new PageInfo(false, null));
        }

        final boolean hasNext = activeDiscussionIds.size() > requestedSize;
        if (hasNext) {
            activeDiscussionIds.removeLast();
        }

        final Discussion lastDiscussion = getLastDiscussion(activeDiscussionIds, hasNext);
        final Long latestCommentIdByDiscussion = commentRepository.findLatestCommentIdByDiscussion(lastDiscussion, periodStart)
                .orElse(null);

        final List<DiscussionResponse> discussionResponses = getDiscussionsResponses(activeDiscussionIds, member);
        final String nextCursor = getNextCursor(hasNext, latestCommentIdByDiscussion);

        return new ActiveDiscussionPageResponse(
                discussionResponses,
                new PageInfo(hasNext, nextCursor)
        );
    }

    private Discussion getLastDiscussion(
            final List<Long> discussions,
            final boolean hasNext
    ) {
        final Long lastDid = discussions.getLast();
        if (hasNext) {
            return discussionRepository.findById(lastDid).get();
        }
        return null;
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
        final Pageable pageable = PageRequest.of(0, size, Sort.Direction.DESC, "id");

        if (cursor == null || cursor.isBlank()) {
            return discussionRepository.findAllIdsBy(pageable);
        }

        final Long cursorId = decodeCursor(cursor);
        return discussionRepository.findIdsLessThan(cursorId, pageable);
    }

    private String processBlankCursor(final String cursor) {
        if (cursor == null || cursor.isBlank()) {
            return null;
        }
        return cursor;
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
        final String keywordWithPrefix = String.format("+%s*", keyword);
        final List<Long> discussionIds = discussionRepository.searchIdsByKeyword(keywordWithPrefix);

        return getDiscussionsResponses(discussionIds, member);
    }

    private List<DiscussionResponse> getDiscussionsResponses(
            final List<Long> discussionIds,
            final Member member
    ) {
        if (discussionIds.isEmpty()) {
            return List.of();
        }

        final List<DiscussionLikeSummaryDto> likeSummaries = discussionLikeRepository.findLikeSummaryByDiscussionIds(member, discussionIds);
        final List<DiscussionCommentCountDto> commentCounts = commentRepository.findCommentCountsByDiscussionIds(discussionIds);

        final Map<Long, LikeCountAndIsLikedByMeDto> likesByDiscussionId = mapLikeSummariesByDiscussionId(likeSummaries);
        final Map<Long, Integer> commentsByDiscussionId = mapTotalCommentCountsByDiscussionId(commentCounts);

        return makeResponsesFrom(discussionIds, likesByDiscussionId, commentsByDiscussionId);
    }

    private List<DiscussionResponse> makeResponsesFrom(
            final List<Long> discussionIds,
            final Map<Long, LikeCountAndIsLikedByMeDto> likeSummaryByDiscussionId,
            final Map<Long, Integer> commentCountsByDiscussionId
    ) {
        final Map<Long, Discussion> discussions = discussionRepository.findDiscussionsInIds(discussionIds).stream()
                .collect(Collectors.toMap(Discussion::getId, discussion -> discussion));

        return discussionIds.stream()
                .map(discussionId -> {
                    final Discussion discussion = discussions.get(discussionId);
                    final int likeCount = likeSummaryByDiscussionId.get(discussionId).likeCount();
                    final int commentCount = commentCountsByDiscussionId.getOrDefault(discussionId, 0);
                    final boolean isLikedByMe = likeSummaryByDiscussionId.get(discussionId).isLikedByMe();
                    return new DiscussionResponse(discussion, likeCount, commentCount, isLikedByMe);
                })
                .toList();

    }

    private Map<Long, Integer> mapTotalCommentCountsByDiscussionId(final List<DiscussionCommentCountDto> commentCounts) {
        return commentCounts.stream()
                .collect(Collectors.toMap(
                        DiscussionCommentCountDto::discussionId,
                        dto -> dto.commentCount() + dto.replyCount()
                ));
    }

    private Map<Long, LikeCountAndIsLikedByMeDto> mapLikeSummariesByDiscussionId(final List<DiscussionLikeSummaryDto> likeCounts) {
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
            final Map<Long, LikeCountAndIsLikedByMeDto> likesByDiscussionId,
            final Map<Long, Integer> commentsByDiscussionId,
            final List<Long> discussionIds
    ) {
        final ToIntFunction<Long> totalCountByDiscussion =
                discussionId ->
                        likesByDiscussionId.get(discussionId).likeCount()
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
