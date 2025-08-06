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

    public List<CommentResponse> getComments(
            final Long memberId,
            final Long discussionId
    ) {
        validateIsExistMember(memberId);
        final Discussion discussion = getDiscussion(discussionId);

        final List<Comment> comments = commentRepository.findCommentsByDiscussion(discussion);
        final List<Long> commentIds = comments.stream()
                .map(Comment::getId)
                .toList();

        final List<CommentLikeCountDto> likeCountsById = commentLikeRepository.findLikeCountsByCommentIds(commentIds);
        final List<CommentReplyCountDto> replyCountsById = replyRepository.findReplyCountsByCommentIds(commentIds);

        return comments.stream()
                .map(comment -> new CommentResponse(
                        comment,
                        findLikeCount(comment, likeCountsById),
                        findReplyCount(comment, replyCountsById)
                ))
                .toList();
    }

    private void validateIsExistMember(final Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new NoSuchElementException("해당 회원을 찾을 수 없습니다");
        }
    }

    private Discussion getDiscussion(final Long discussionId) {
        return discussionRepository.findById(discussionId)
                .orElseThrow(() -> new NoSuchElementException("해당 토론방을 찾을 수 없습니다"));
    }

    private static int findReplyCount(
            final Comment comment,
            final List<CommentReplyCountDto> replyCountsById
    ) {
        return replyCountsById.stream()
                .filter(count -> comment.isSameId(count.commentId()))
                .findFirst()
                .map(CommentReplyCountDto::replyCount)
                .orElseThrow(() -> new IllegalStateException("댓글의 대댓글 수를 찾을 수 없습니다"));
    }

    private static int findLikeCount(
            final Comment comment,
            final List<CommentLikeCountDto> likeCountsById
    ) {
        return likeCountsById.stream()
                .filter(count -> comment.isSameId(count.commentId()))
                .findFirst()
                .map(CommentLikeCountDto::likeCount)
                .orElseThrow(() -> new IllegalStateException("댓글의 좋아요 수를 찾을 수 없습니다"));
    }
}
