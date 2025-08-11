package todoktodok.backend.discussion.application.service.query;

import java.util.List;
import java.util.NoSuchElementException;
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

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class DiscussionQueryService {

    private final DiscussionRepository discussionRepository;
    private final DiscussionLikeRepository discussionLikeRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public DiscussionResponse getDiscussion(
            final Long memberId,
            final Long discussionId
    ) {
        final Member member = findMember(memberId);
        final Discussion discussion = findDiscussion(discussionId);

        final int likeCount = Math.toIntExact(discussionLikeRepository.findLikeCountsByDiscussionId(discussionId));
        final int commentCount = Math.toIntExact(commentRepository.findCommentCountsByDiscussionId(discussionId));
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
                .orElseThrow(() -> new NoSuchElementException("해당 토론방을 찾을 수 없습니다"));
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 회원을 찾을 수 없습니다"));
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
        final List<Long> likedDiscussionIds = discussionLikeRepository.findLikedDiscussionIdsByMember(member, discussionIds);

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
                .orElseThrow(() -> new IllegalStateException("토론방의 댓글 수를 찾을 수 없습니다"));
    }

    private int findLikeCount(
            final Discussion discussion,
            final List<DiscussionLikeCountDto> likeCountsById
    ) {
        return likeCountsById.stream()
                .filter(count -> discussion.isSameId(count.discussionId()))
                .findFirst()
                .map(DiscussionLikeCountDto::likeCount)
                .orElseThrow(() -> new IllegalStateException("토론방의 좋아요 수를 찾을 수 없습니다"));
    }

    private boolean checkIsLikedByMe(
            final Discussion discussion,
            final List<Long> likedDiscussionIds
    ) {
        return likedDiscussionIds.contains(discussion.getId());
    }
}
