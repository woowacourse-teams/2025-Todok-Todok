package todoktodok.backend.reply.application.service.command;

import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.comment.domain.repository.CommentRepository;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.repository.DiscussionRepository;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.reply.application.dto.request.ReplyRequest;
import todoktodok.backend.reply.domain.Reply;
import todoktodok.backend.reply.domain.repository.ReplyRepository;

@Service
@Transactional
@AllArgsConstructor
public class ReplyCommandService {

    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final DiscussionRepository discussionRepository;
    private final CommentRepository commentRepository;

    public Long createReply(
            final Long memberId,
            final Long discussionId,
            final Long commentId,
            final ReplyRequest replyRequest
    ) {
        final Member member = findMember(memberId);
        final Discussion discussion = findDiscussion(discussionId);
        final Comment comment = findComment(commentId);

        comment.validateMatchWithDiscussion(discussion);

        final Reply reply = Reply.builder()
                .content(replyRequest.content())
                .member(member)
                .comment(comment)
                .build();

        final Reply savedReply = replyRepository.save(reply);
        return savedReply.getId();
    }


    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 회원을 찾을 수 없습니다"));
    }

    private Discussion findDiscussion(final Long discussionId) {
        return discussionRepository.findById(discussionId)
                .orElseThrow(() -> new NoSuchElementException("해당 토론방을 찾을 수 없습니다"));
    }

    private Comment findComment(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("해당 댓글을 찾을 수 없습니다"));
    }
}
