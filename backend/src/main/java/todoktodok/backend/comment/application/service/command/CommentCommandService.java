package todoktodok.backend.comment.application.service.command;

import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.comment.application.dto.request.CommentRequest;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.comment.domain.repository.CommentRepository;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.repository.DiscussionRepository;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;

@Service
@Transactional
@AllArgsConstructor
public class CommentCommandService {

    private final CommentRepository commentRepository;
    private final DiscussionRepository discussionRepository;
    private final MemberRepository memberRepository;

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

    private Discussion findDiscussion(final Long discussionId) {
        return discussionRepository.findById(discussionId)
                .orElseThrow(() -> new NoSuchElementException("해당 토론방을 찾을 수 없습니다"));
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 회원을 찾을 수 없습니다"));
    }
}
