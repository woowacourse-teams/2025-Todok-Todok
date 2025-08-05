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

        return commentRepository.findCommentsByDiscussion(discussion).stream()
                .map(this::getCommentResponseWithCount)
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

    private CommentResponse getCommentResponseWithCount(final Comment comment) {
        final int likeCount = commentLikeRepository.countCommentLikesByComment(comment);
        final int replyCount = replyRepository.countRepliesByComment(comment);

        return new CommentResponse(comment, likeCount, replyCount);
    }
}
