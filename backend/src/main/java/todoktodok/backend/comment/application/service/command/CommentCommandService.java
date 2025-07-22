package todoktodok.backend.comment.application.service.command;

import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.comment.application.dto.request.CommentRequest;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.comment.domain.CommentReport;
import todoktodok.backend.comment.domain.repository.CommentReportRepository;
import todoktodok.backend.comment.domain.repository.CommentRepository;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.repository.DiscussionRepository;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;

@Service
@Transactional
@AllArgsConstructor
public class CommentCommandService {

    private final MemberRepository memberRepository;
    private final DiscussionRepository discussionRepository;
    private final CommentRepository commentRepository;
    private final CommentReportRepository commentReportRepository;

    public Long createComment(
            final Long memberId,
            final Long discussionId,
            final CommentRequest commentRequest
    ) {
        final Member member = getMember(memberId);
        final Discussion discussion = getDiscussion(discussionId);

        final Comment comment = Comment.builder()
                .content(commentRequest.content())
                .member(member)
                .discussion(discussion)
                .build();

        final Comment savedComment = commentRepository.save(comment);
        return savedComment.getId();
    }

    public void report(
            final Long memberId,
            final Long discussionId,
            final Long commentId
    ) {
        final Member member = getMember(memberId);
        final Comment comment = getComment(commentId);
        final Discussion discussion = getDiscussion(discussionId);

        comment.validateMatchWithDiscussion(discussion);

//        validateSelfReport(member, comment);
        comment.validateSelfReport(member);
        validateDuplicatedReport(member, comment);

        final CommentReport commentReport = CommentReport.builder()
                .comment(comment)
                .member(member)
                .build();

        commentReportRepository.save(commentReport);
    }

    private Comment getComment(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("해당 댓글을 찾을 수 없습니다"));
    }

    private Discussion getDiscussion(final Long discussionId) {
        return discussionRepository.findById(discussionId)
                .orElseThrow(() -> new NoSuchElementException("해당 토론방을 찾을 수 없습니다"));
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 회원을 찾을 수 없습니다"));
    }

    private void validateSelfReport(
            final Member member,
            final Comment comment
    ) {
        if (comment.getMember().equals(member)) {
            throw new IllegalArgumentException("자기 자신이 작성한 댓글을 신고할 수 없습니다");
        }
    }

    private void validateDuplicatedReport(
            final Member member,
            final Comment comment
    ) {
        if (commentReportRepository.existsByMemberAndComment(member, comment)) {
            throw new IllegalArgumentException("이미 신고한 댓글입니다");
        }
    }
}
