package todoktodok.backend.note.application.service.query;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.book.domain.repository.BookRepository;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.note.application.dto.response.MyNoteResponse;
import todoktodok.backend.note.domain.Note;
import todoktodok.backend.note.domain.repository.NoteRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class NoteQueryService {

    private final NoteRepository noteRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    public List<MyNoteResponse> getMyNotes(
            final Long memberId,
            final Long bookId
    ) {
        final Member member = findMember(memberId);

        if (bookId == null) {
            return getMyNotesAll(member);
        }
        return getMyNotesByBookId(member, bookId);
    }

    public MyNoteResponse getMyNote(
            final Long memberId,
            final Long noteId
    ) {
        Note note = findNote(noteId);
        validateIsMyNote(note, memberId);

        return new MyNoteResponse(note);
    }

    private List<MyNoteResponse> getMyNotesAll(final Member member) {
        return noteRepository.findNotesByMember(member).stream()
                .map(MyNoteResponse::new)
                .toList();
    }

    private List<MyNoteResponse> getMyNotesByBookId(
            final Member member,
            final Long bookId
    ) {
        Book book = findBook(bookId);

        return noteRepository.findNotesByMemberAndBook(member, book).stream()
                .map(MyNoteResponse::new)
                .toList();
    }

    private void validateIsMyNote(
            final Note note,
            final Long memberId
    ) {
        Member member = findMember(memberId);
        if (!note.isOwnedBy(member)) {
            throw new IllegalArgumentException("자신의 기록만 조회 가능합니다");
        }
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 회원을 찾을 수 없습니다"));
    }

    private Note findNote(Long noteId) {
        return noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 기록을 찾을 수 없습니다"));
    }

    private Book findBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 도서를 찾을 수 없습니다"));
    }
}
