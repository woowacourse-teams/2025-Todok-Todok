package todoktodok.backend.comment.application.service.command;

import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.comment.application.dto.request.CommentRequest;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.comment.domain.CommentLike;
import todoktodok.backend.comment.domain.CommentReport;
import todoktodok.backend.comment.domain.repository.CommentLikeRepository;
import todoktodok.backend.comment.domain.repository.CommentReportRepository;
import todoktodok.backend.comment.domain.repository.CommentRepository;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.repository.DiscussionRepository;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.reply.domain.repository.ReplyRepository;

@Service
@Transactional
@AllArgsConstructor
public class CommentCommandService {

    private final MemberRepository memberRepository;
    private final DiscussionRepository discussionRepository;
    private final CommentRepository commentRepository;
    private final CommentReportRepository commentReportRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ReplyRepository replyRepository;

    public Long createComment(
            final Long memberId,
            final Long discussionId,
            final CommentRequest commentRequest
    ) {
        final Member member = findMember(memberId);
        final Discussion discussion = findDiscussion(discussionId);

        final Comment comment = Comment.builder()
                .content(commentRequest.content())
                .member(member)
                .discussion(discussion)
                .build();

        final Comment savedComment = commentRepository.save(comment);
        return savedComment.getId();
    }

    public boolean toggleLike(
            final Long memberId,
            final Long discussionId,
            final Long commentId
    ) {
        final Member member = findMember(memberId);
        final Comment comment = findComment(commentId);
        final Discussion discussion = findDiscussion(discussionId);

        comment.validateMatchWithDiscussion(discussion);

        final Optional<CommentLike> commentLikeOrEmpty = commentLikeRepository.findByMemberAndComment(member, comment);
        if (commentLikeOrEmpty.isPresent()) {
            commentLikeRepository.delete(commentLikeOrEmpty.get());
            return false;
        }

        final CommentLike commentLike = CommentLike.builder()
                .comment(comment)
                .member(member)
                .build();

        commentLikeRepository.save(commentLike);
        return true;
    }

    public void report(
            final Long memberId,
            final Long discussionId,
            final Long commentId,
            final String reason
    ) {
        final Member member = findMember(memberId);
        final Comment comment = findComment(commentId);
        final Discussion discussion = findDiscussion(discussionId);

        comment.validateMatchWithDiscussion(discussion);
        comment.validateSelfReport(member);

        validateDuplicatedReport(member, comment);

        final CommentReport commentReport = CommentReport.builder()
                .comment(comment)
                .member(member)
                .reason(reason)
                .build();

        commentReportRepository.save(commentReport);
    }

    public void updateComment(
            final Long memberId,
            final Long discussionId,
            final Long commentId,
            final CommentRequest commentRequest
    ) {
        final Comment comment = findComment(commentId);
        final Member member = findMember(memberId);
        final Discussion discussion = findDiscussion(discussionId);

        validateCommentMember(comment, member);
        comment.validateMatchWithDiscussion(discussion);

        comment.updateContent(commentRequest.content());
    }

    public void deleteComment(
            final Long memberId,
            final Long discussionId,
            final Long commentId
    ) {
        final Comment comment = findComment(commentId);
        final Member member = findMember(memberId);
        final Discussion discussion = findDiscussion(discussionId);

        validateHasReply(comment);
        validateCommentMember(comment, member);
        comment.validateMatchWithDiscussion(discussion);

        commentRepository.delete(comment);
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException(String.format("해당 회원을 찾을 수 없습니다: memberId = %s", memberId)));
    }

    private Comment findComment(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException(String.format("해당 댓글을 찾을 수 없습니다: commentId = %s", commentId)));
    }

    private Discussion findDiscussion(final Long discussionId) {
        return discussionRepository.findById(discussionId)
                .orElseThrow(() -> new NoSuchElementException(String.format("해당 토론방을 찾을 수 없습니다: discussionId = %s", discussionId)));
    }

    private void validateDuplicatedReport(
            final Member member,
            final Comment comment
    ) {
        if (commentReportRepository.existsByMemberAndComment(member, comment)) {
            throw new IllegalArgumentException(String.format("이미 신고한 댓글입니다: memberId = %s -> commentId = %s", member.getId(), comment.getId()));
        }
    }

    private void validateCommentMember(
            final Comment comment,
            final Member member
    ) {
        if (!comment.isOwnedBy(member)) {
            throw new IllegalArgumentException(String.format("자기 자신의 댓글만 수정/삭제 가능합니다: memberId = %s, commentId = %s",
                    member.getId(), comment.getId()));
        }
    }

    private void validateHasReply(final Comment comment) {
        if (replyRepository.existsByComment(comment)) {
            throw new IllegalArgumentException(String.format("대댓글이 존재하는 댓글은 삭제할 수 없습니다: commentId = %s", comment.getId()));
        }
    }
}
