package todoktodok.backend.comment.application.service.query;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.comment.application.dto.response.CommentResponse;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.comment.domain.repository.CommentLikeRepository;
import todoktodok.backend.comment.domain.repository.CommentRepository;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.repository.DiscussionRepository;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.reply.domain.repository.ReplyRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CommentQueryService {

    private final MemberRepository memberRepository;
    private final DiscussionRepository discussionRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ReplyRepository replyRepository;

    public CommentResponse getComment(
            final Long memberId,
            final Long discussionId,
            final Long commentId
    ) {
        final Discussion discussion = findDiscussion(discussionId);
        final Comment comment = findComment(commentId);

        validateIsExistMember(memberId);
        comment.validateMatchWithDiscussion(discussion);

        final int likeCount = commentLikeRepository.findLikeCountsByCommentIds(List.of(comment.getId()))
                .getFirst()
                .likeCount();
        final int replyCount = replyRepository.countRepliesByComment(comment);
        final boolean isLikedByMe = commentLikeRepository.existsByMemberIdAndCommentId(memberId, commentId);

        return new CommentResponse(comment, likeCount, replyCount, isLikedByMe);
    }

    public List<CommentResponse> getComments(
            final Long memberId,
            final Long discussionId
    ) {
        validateIsExistMember(memberId);
        final Discussion discussion = findDiscussion(discussionId);

        final List<Comment> comments = commentRepository.findCommentsByDiscussion(discussion);
        final List<Long> commentIds = comments.stream()
                .map(Comment::getId)
                .toList();

        final List<CommentLikeCountDto> likeCountsById = commentLikeRepository.findLikeCountsByCommentIds(commentIds);
        final List<CommentReplyCountDto> replyCountsById = replyRepository.findReplyCountsByCommentIds(commentIds);
        final List<Long> likedCommentIds = commentLikeRepository.findLikedCommentIdsByMember(memberId, commentIds);

        return comments.stream()
                .map(comment -> new CommentResponse(
                        comment,
                        findLikeCount(comment, likeCountsById),
                        findReplyCount(comment, replyCountsById),
                        checkIsLikedByMe(comment, likedCommentIds)
                ))
                .toList();
    }

    private void validateIsExistMember(final Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new NoSuchElementException("해당 회원을 찾을 수 없습니다");
        }
    }

    private Discussion findDiscussion(final Long discussionId) {
        return discussionRepository.findById(discussionId)
                .orElseThrow(() -> new NoSuchElementException("해당 토론방을 찾을 수 없습니다"));
    }

    private Comment findComment(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("해당 댓글을 찾을 수 없습니다"));
    }

    private int findReplyCount(
            final Comment comment,
            final List<CommentReplyCountDto> replyCountsById
    ) {
        return replyCountsById.stream()
                .filter(count -> comment.isSameId(count.commentId()))
                .findFirst()
                .map(CommentReplyCountDto::replyCount)
                .orElseThrow(() -> new IllegalStateException("댓글의 대댓글 수를 찾을 수 없습니다"));
    }

    private int findLikeCount(
            final Comment comment,
            final List<CommentLikeCountDto> likeCountsById
    ) {
        return likeCountsById.stream()
                .filter(count -> comment.isSameId(count.commentId()))
                .findFirst()
                .map(CommentLikeCountDto::likeCount)
                .orElseThrow(() -> new IllegalStateException("댓글의 좋아요 수를 찾을 수 없습니다"));
    }

    private boolean checkIsLikedByMe(
            final Comment comment,
            final List<Long> likedCommentIds
    ) {
        return likedCommentIds.contains(comment.getId());
    }
}
