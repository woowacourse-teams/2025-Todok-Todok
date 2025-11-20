package todoktodok.backend.reply.application.service.command;

import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.comment.domain.repository.CommentRepository;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.repository.DiscussionRepository;
import todoktodok.backend.global.report.ContentReportReason;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.reply.application.dto.request.ReplyRequest;
import todoktodok.backend.reply.domain.Reply;
import todoktodok.backend.reply.domain.ReplyLike;
import todoktodok.backend.reply.domain.ReplyReport;
import todoktodok.backend.reply.domain.repository.ReplyLikeRepository;
import todoktodok.backend.reply.domain.repository.ReplyReportRepository;
import todoktodok.backend.reply.domain.repository.ReplyRepository;

@Service
@Transactional
@AllArgsConstructor
public class ReplyCommandService {

    private final ReplyRepository replyRepository;
    private final ReplyReportRepository replyReportRepository;
    private final ReplyLikeRepository replyLikeRepository;
    private final MemberRepository memberRepository;
    private final DiscussionRepository discussionRepository;
    private final CommentRepository commentRepository;

    private final ApplicationEventPublisher publisher;

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
        discussionRepository.increaseCommentCount(discussionId);
        publisher.publishEvent(new ReplyCreated(discussion, comment, savedReply, member));

        return savedReply.getId();
    }

    public void report(
            final Long memberId,
            final Long discussionId,
            final Long commentId,
            final Long replyId,
            final String reason
    ) {
        final Member member = findMember(memberId);
        final Discussion discussion = findDiscussion(discussionId);
        final Comment comment = findComment(commentId);
        final Reply reply = findReply(replyId);
        final ContentReportReason reportReason = ContentReportReason.fromDescription(reason);

        reply.validateMatchWithComment(comment);
        reply.validateSelfReport(member);
        comment.validateMatchWithDiscussion(discussion);

        validateDuplicatedReport(member, reply);

        final ReplyReport replyReport = ReplyReport.builder()
                .reply(reply)
                .member(member)
                .reason(reportReason)
                .build();

        replyReportRepository.save(replyReport);
    }

    public void updateReply(
            final Long memberId,
            final Long discussionId,
            final Long commentId,
            final Long replyId,
            final ReplyRequest replyRequest
    ) {
        final Member member = findMember(memberId);
        final Discussion discussion = findDiscussion(discussionId);
        final Comment comment = findComment(commentId);
        final Reply reply = findReply(replyId);

        comment.validateMatchWithDiscussion(discussion);
        reply.validateMatchWithComment(comment);
        validateReplyMember(reply, member);

        reply.updateContent(replyRequest.content());
    }

    public void deleteReply(
            final Long memberId,
            final Long discussionId,
            final Long commentId,
            final Long replyId
    ) {
        final Member member = findMember(memberId);
        final Comment comment = findComment(commentId);
        final Discussion discussion = findDiscussion(discussionId);
        final Reply reply = findReply(replyId);

        validateReplyMember(reply, member);
        comment.validateMatchWithDiscussion(discussion);
        reply.validateMatchWithComment(comment);

        replyRepository.delete(reply);
        discussionRepository.decreaseCommentCount(discussionId);
    }

    public boolean toggleLike(
            final Long memberId,
            final Long discussionId,
            final Long commentId,
            final Long replyId
    ) {
        final Member member = findMember(memberId);
        final Discussion discussion = findDiscussion(discussionId);
        final Comment comment = findComment(commentId);
        final Reply reply = findReply(replyId);

        comment.validateMatchWithDiscussion(discussion);
        reply.validateMatchWithComment(comment);

        final Optional<ReplyLike> replyLikeOrEmpty = replyLikeRepository.findByMemberAndReply(member, reply);
        if (replyLikeOrEmpty.isPresent()) {
            replyLikeRepository.delete(replyLikeOrEmpty.get());
            return false;
        }

        final ReplyLike replyLike = ReplyLike.builder()
                .reply(reply)
                .member(member)
                .build();

        replyLikeRepository.save(replyLike);
        publisher.publishEvent(new ReplyLikeCreated(member, discussion, comment, reply));

        return true;
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new NoSuchElementException(String.format("해당 회원을 찾을 수 없습니다: memberId = %s", memberId)));
    }

    private Discussion findDiscussion(final Long discussionId) {
        return discussionRepository.findById(discussionId)
                .orElseThrow(() -> new NoSuchElementException(String.format("해당 토론방을 찾을 수 없습니다: discussionId = %s", discussionId)));
    }

    private Comment findComment(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException(String.format("해당 댓글을 찾을 수 없습니다: commentId = %s", commentId)));
    }

    private Reply findReply(final Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new NoSuchElementException(String.format("해당 대댓글을 찾을 수 없습니다: replyId = %s", replyId)));
    }

    private void validateDuplicatedReport(
            final Member member,
            final Reply reply
    ) {
        if (replyReportRepository.existsByMemberAndReply(member, reply)) {
            throw new IllegalArgumentException(String.format("이미 신고한 대댓글입니다: memberId = %s -> replyId = %s", member.getId(), reply.getId()));
        }
    }

    private void validateReplyMember(
            final Reply reply,
            final Member member
    ) {
        if (!reply.isOwnedBy(member)) {
            throw new IllegalArgumentException(String.format("자기 자신의 대댓글만 수정/삭제 가능합니다: memberId = %s, replyId = %s"
                    , member.getId(), reply.getId()));
        }
    }
}
