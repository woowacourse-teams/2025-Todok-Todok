package todoktodok.backend.discussion.application.service.query;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.comment.domain.repository.CommentRepository;
import todoktodok.backend.discussion.application.dto.DiscussionCursor;
import todoktodok.backend.discussion.application.dto.response.DiscussionPageResponse;
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

    private static final int MIN_PAGE_SIZE = 1;

    private final DiscussionRepository discussionRepository;
    private final DiscussionLikeRepository discussionLikeRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final Clock clock;

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

        final List<DiscussionLikeCountDto> likeCountsById = discussionLikeRepository.findLikeCountsByDiscussionIds(
                discussionIds);
        final List<DiscussionCommentCountDto> commentCountsById = commentRepository.findCommentCountsByDiscussionIds(
                discussionIds);
        final List<Long> likedDiscussionIds = discussionLikeRepository.findLikedDiscussionIdsByMember(member,
                discussionIds);

        return discussions.stream()
                .map(discussion -> new DiscussionResponse(
                        discussion,
                        findLikeCount(discussion, likeCountsById),
                        findCommentCount(discussion, commentCountsById),
                        checkIsLikedByMe(discussion, likedDiscussionIds)
                ))
                .toList();
    }

    private int findCommentCount(
            final Discussion discussion,
            final List<DiscussionCommentCountDto> commentCountsById
    ) {
        return commentCountsById.stream()
                .filter(count -> discussion.isSameId(count.discussionId()))
                .findFirst()
                .map(dto -> dto.commentCount() + dto.replyCount())
                .orElseThrow(() -> new IllegalStateException(
                                String.format("토론방의 댓글 수를 찾을 수 없습니다: discussionId= %s", discussion.getId())
                        )
                );
    }

    private int findLikeCount(
            final Discussion discussion,
            final List<DiscussionLikeCountDto> likeCountsById
    ) {
        return likeCountsById.stream()
                .filter(count -> discussion.isSameId(count.discussionId()))
                .findFirst()
                .map(DiscussionLikeCountDto::likeCount)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("토론방의 좋아요 수를 찾을 수 없습니다: discussionId= %s", discussion.getId()))
                );
    }

    private boolean checkIsLikedByMe(
            final Discussion discussion,
            final List<Long> likedDiscussionIds
    ) {
        return likedDiscussionIds.contains(discussion.getId());
    }

    public DiscussionPageResponse getActiveDiscussions(
            final Long memberId,
            final int period,
            final int size,
            @Nullable final String cursor
    ) {
        validatePageSize(size);

        final Member member = findMember(memberId);
        final LocalDateTime periodStart = LocalDateTime.now(clock).minusDays(period);

        final DiscussionCursor discussionCursor = Optional.ofNullable(cursor)
                .map(DiscussionCursor::fromEncoded)
                .orElse(DiscussionCursor.empty());

        final Pageable pageable = Pageable.ofSize(size + 1);

        final List<DiscussionCursorResponse> discussions = discussionRepository.findActiveDiscussionsByCursor(
                member,
                periodStart,
                discussionCursor.lastCommentedAt(),
                discussionCursor.cursorId(),
                pageable
        );

        if (discussions.isEmpty()) {
            return new DiscussionPageResponse(Collections.emptyList(), false, null);
        }

        final boolean hasNext = discussions.size() > size;
        if (hasNext) {
            discussions.removeLast();
        }

        String nextCursor = null;
        if (hasNext) {
            DiscussionCursorResponse last = discussions.getLast();
            nextCursor = discussionCursor.toEncoded(
                    last.lastCommentedAt(),
                    last.discussionId()
            );
        }

        return new DiscussionPageResponse(discussions, hasNext, nextCursor);
    }

    private void validatePageSize(int size) {
        if (size < MIN_PAGE_SIZE) {
            throw new IllegalArgumentException("[ERROR] 페이지 사이즈는 1 이상이어야 합니다: " + size);
        }
    }
}
