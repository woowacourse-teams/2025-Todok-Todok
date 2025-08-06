package todoktodok.backend.reply.application.service.query;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.comment.domain.repository.CommentRepository;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.repository.DiscussionRepository;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.reply.application.dto.response.ReplyResponse;
import todoktodok.backend.reply.domain.Reply;
import todoktodok.backend.reply.domain.repository.ReplyLikeRepository;
import todoktodok.backend.reply.domain.repository.ReplyRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ReplyQueryService {

    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final DiscussionRepository discussionRepository;
    private final CommentRepository commentRepository;
    private final ReplyLikeRepository replyLikeRepository;

    public List<ReplyResponse> getReplies(
            final Long memberId,
            final Long discussionId,
            final Long commentId
    ) {
        final Discussion discussion = getDiscussion(discussionId);
        final Comment comment = getComment(commentId);

        validateIsExistMember(memberId);
        comment.validateMatchWithDiscussion(discussion);


        final List<Reply> replies = replyRepository.findRepliesByComment(comment);
        final List<Long> replyIds = replies.stream()
                .map(Reply::getId)
                .toList();

        final List<ReplyLikeCountDto> likeCountsById = replyLikeRepository.findLikeCountsByReplyIds(replyIds);

        return replies.stream()
                .map(reply -> new ReplyResponse(
                        reply,
                        getLikeCount(reply, likeCountsById)
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

    private Comment getComment(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("해당 댓글을 찾을 수 없습니다"));
    }

    private static int getLikeCount(
            final Reply reply,
            final List<ReplyLikeCountDto> likeCountsById
    ) {
        return likeCountsById.stream()
                .filter(count -> count.replyId().equals(reply.getId()))
                .findFirst()
                .orElseThrow(IllegalStateException::new)
                .likeCount();
    }
}
