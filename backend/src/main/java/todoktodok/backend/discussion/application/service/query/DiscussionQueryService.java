package todoktodok.backend.discussion.application.service.query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
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

        // Discussion 엔티티의 카운트 컬럼 직접 사용
        final int likeCount = discussion.getLikeCount();
        final int commentCount = discussion.getCommentCount();
        final boolean isLikedByMe = discussionLikeRepository.existsByMemberAndDiscussion(member, discussion);

        eventPublisher.publishEvent(discussionViewEvent);

        return new DiscussionResponse(discussion, likeCount, commentCount, isLikedByMe);
    }

    public LatestDiscussionPageResponse getDiscussions(
            final Long memberId,
            final int size,
            final String cursor
    ) {
        validatePageSize(size);
        final Member member = findMember(memberId);

        final Slice<Long> discussionIdsSlice = sliceDiscussionsBy(cursor, size);

        return createPageResponse(discussionIdsSlice, member);
    }

    public List<DiscussionResponse> getDiscussionsByKeyword(
            final Long memberId,
            final String keyword
    ) {
        validateKeywordNotBlank(keyword);

        final Member member = findMember(memberId);
        return getDiscussionsByKeyword(keyword, member);
    }

    public LatestDiscussionPageResponse getDiscussionsByKeywordWithPagination(
            final Long memberId,
            final String keyword,
            final int size,
            final String cursor
    ) {
        validateKeywordNotBlank(keyword);
        validatePageSize(size);

        final Member member = findMember(memberId);
        final String keywordWithPrefix = String.format("+%s*", keyword);

        // 전체 개수 조회
        final long totalCount = discussionRepository.countSearchResultsByKeyword(keywordWithPrefix);

        // 페이지네이션된 결과 조회
        final Slice<Long> discussionIdsSlice = sliceDiscussionsByKeyword(keyword, cursor, size);

        return createPageResponseWithTotalCount(discussionIdsSlice, member, totalCount);
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
        final Slice<Long> discussionIdsSlice = sliceDiscussionsByBook(bookId, cursor, pageable);

        return createPageResponse(discussionIdsSlice, member);
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
        final Pageable pageable = PageRequest.of(0, count);

        // discussion 테이블의 likeCount + commentCount 컬럼으로 정렬
        final List<Long> hotDiscussionIds = discussionRepository.findHotDiscussionIdsSinceDate(sinceDate, pageable);

        return getDiscussionsResponses(hotDiscussionIds, member);
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
        final Long latestCommentIdByDiscussion = commentRepository.findLatestCommentIdByDiscussion(lastDiscussion,
                        periodStart)
                .orElse(null);

        final List<DiscussionResponse> discussionResponses = getDiscussionsResponses(activeDiscussionIds, member);
        final String nextCursor = getNextCursor(hasNext, latestCommentIdByDiscussion);

        return new ActiveDiscussionPageResponse(
                discussionResponses,
                new PageInfo(hasNext, nextCursor)
        );
    }

    public List<DiscussionResponse> getLikedDiscussionsByMe(final Long memberId) {
        final Member member = findMember(memberId);
        final List<Long> likedIds = discussionLikeRepository.findLikedDiscussionIdsByMember(member);

        return getDiscussionsResponses(likedIds, member);
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

        // isLikedByMe 정보만 조회 (likeCount와 commentCount는 Discussion 엔티티에서 직접 가져옴)
        final List<Long> likedDiscussionIds = discussionLikeRepository.findLikedDiscussionIdsByMemberAndDiscussionIds(
                member, discussionIds);
        final Map<Long, Boolean> isLikedByMeMap = discussionIds.stream()
                .collect(Collectors.toMap(id -> id, likedDiscussionIds::contains));

        return makeResponsesFrom(discussionIds, isLikedByMeMap);
    }

    private List<DiscussionResponse> makeResponsesFrom(
            final List<Long> discussionIds,
            final Map<Long, Boolean> isLikedByMeMap
    ) {
        final Map<Long, Discussion> discussions = discussionRepository.findDiscussionsInIds(discussionIds).stream()
                .collect(Collectors.toMap(Discussion::getId, discussion -> discussion));

        return discussionIds.stream()
                .map(discussionId -> {
                    final Discussion discussion = discussions.get(discussionId);
                    // Discussion 엔티티의 카운트 컬럼 직접 사용
                    final int likeCount = discussion.getLikeCount();
                    final int commentCount = discussion.getCommentCount();
                    final boolean isLikedByMe = isLikedByMeMap.getOrDefault(discussionId, false);
                    return new DiscussionResponse(discussion, likeCount, commentCount, isLikedByMe);
                })
                .toList();
    }

    private static void validateHotDiscussionCount(final int count) {
        if (count < MIN_HOT_DISCUSSION_COUNT) {
            throw new IllegalArgumentException(String.format("유효하지 않은 개수입니다. 양수의 개수를 조회해주세요: count = %d", count));
        }
    }

    private static void validateDiscussionPeriod(final int period) {
        if (period < MIN_DISCUSSION_PERIOD || period > MAX_DISCUSSION_PERIOD) {
            throw new IllegalArgumentException(
                    String.format("유효하지 않은 기간 값입니다. 0일 ~ 7일 이내로 조회해주세요: period = %d", period));
        }
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

    private Slice<Long> sliceDiscussionsByBook(
            final Long bookId,
            final String cursor,
            final Pageable pageable
    ) {
        if (cursor == null || cursor.isBlank()) {
            return discussionRepository.findIdsByBookId(bookId, pageable);
        }

        final Long cursorId = decodeCursor(cursor);
        return discussionRepository.findIdsByBookIdLessThan(bookId, cursorId, pageable);
    }

    private Slice<Long> sliceDiscussionsByKeyword(
            final String keyword,
            final String cursor,
            final int size
    ) {
        final String keywordWithPrefix = String.format("+%s*", keyword);
        final Pageable pageable = PageRequest.of(0, size, Direction.DESC, "id");
        final Long cursorId = (cursor == null || cursor.isBlank()) ? null : decodeCursor(cursor);

        return discussionRepository.searchIdsByKeywordWithCursor(keywordWithPrefix, cursorId, pageable);
    }

    private LatestDiscussionPageResponse createPageResponse(
            final Slice<Long> discussionIdsSlice,
            final Member member
    ) {
        final List<Long> discussionIds = discussionIdsSlice.getContent();
        final boolean hasNextPage = discussionIdsSlice.hasNext();
        final String nextCursor = findNextCursor(hasNextPage, discussionIds);

        return new LatestDiscussionPageResponse(
                getDiscussionsResponses(discussionIds, member),
                new PageInfo(hasNextPage, nextCursor)
        );
    }

    private LatestDiscussionPageResponse createPageResponseWithTotalCount(
            final Slice<Long> discussionIdsSlice,
            final Member member,
            final long totalCount
    ) {
        final List<Long> discussionIds = discussionIdsSlice.getContent();
        final boolean hasNextPage = discussionIdsSlice.hasNext();
        final String nextCursor = findNextCursor(hasNextPage, discussionIds);

        return new LatestDiscussionPageResponse(
                getDiscussionsResponses(discussionIds, member),
                new PageInfo(hasNextPage, nextCursor, totalCount)
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
