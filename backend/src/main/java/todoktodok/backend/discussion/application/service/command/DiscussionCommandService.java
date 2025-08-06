package todoktodok.backend.discussion.application.service.command;

import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.book.domain.repository.BookRepository;
import todoktodok.backend.comment.application.dto.request.CommentRequest;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.discussion.application.dto.request.DiscussionRequest;
import todoktodok.backend.discussion.application.dto.request.DiscussionUpdateRequest;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.DiscussionReport;
import todoktodok.backend.discussion.domain.repository.DiscussionReportRepository;
import todoktodok.backend.discussion.domain.repository.DiscussionRepository;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;

@Service
@Transactional
@AllArgsConstructor
public class DiscussionCommandService {

    private final DiscussionRepository discussionRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final DiscussionReportRepository discussionReportRepository;

    public Long createDiscussion(
            final Long memberId,
            final DiscussionRequest discussionRequest
    ) {
        final Member member = findMember(memberId);
        final Book book = findBook(discussionRequest.bookId());

        final Discussion discussion = Discussion.builder()
                .title(discussionRequest.discussionTitle())
                .content(discussionRequest.discussionOpinion())
                .member(member)
                .book(book)
                .build();

        final Discussion savedDiscussion = discussionRepository.save(discussion);
        return savedDiscussion.getId();
    }

    public void report(
            final Long memberId,
            final Long discussionId
    ) {
        final Discussion discussion = findDiscussion(discussionId);
        final Member member = findMember(memberId);

        validateDuplicatedReport(discussion, member);
        validateSelfReport(discussion, member);

        final DiscussionReport discussionReport = DiscussionReport.builder()
                .discussion(discussion)
                .member(member)
                .build();

        discussionReportRepository.save(discussionReport);
    }

    public void updateDiscussion(
            final Long memberId,
            final Long discussionId,
            final DiscussionUpdateRequest discussionUpdateRequest
    ) {
        final Member member = findMember(memberId);
        final Discussion discussion = findDiscussion(discussionId);

        validateDiscussionMember(discussion, member);

        discussion.update(discussionUpdateRequest.discussionTitle(), discussionUpdateRequest.discussionOpinion());
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 회원을 찾을 수 없습니다"));
    }

    private Book findBook(final Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("해당 도서를 찾을 수 없습니다"));
    }

    private Discussion findDiscussion(final Long discussionId) {
        return discussionRepository.findById(discussionId)
                .orElseThrow(() -> new NoSuchElementException("해당 토론방을 찾을 수 없습니다"));
    }

    private void validateDiscussionMember(
            final Discussion discussion,
            final Member member
    ) {
        if (!discussion.isOwnedBy(member)) {
            throw new IllegalArgumentException("자기 자신의 토론방만 수정/삭제 가능합니다");
        }
    }

    private void validateDuplicatedReport(
            final Discussion discussion,
            final Member member
    ) {
        if (discussionReportRepository.existsByDiscussionAndMember(discussion, member)) {
            throw new IllegalArgumentException("이미 신고한 토론방입니다");
        }
    }

    private void validateSelfReport(
            final Discussion discussion,
            final Member member
    ) {
        if (discussion.isOwnedBy(member)) {
            throw new IllegalArgumentException("자기 자신의 토론방을 신고할 수 없습니다");
        }
    }
}
