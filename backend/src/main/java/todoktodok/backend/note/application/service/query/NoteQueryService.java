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
import todoktodok.backend.shelf.domain.repository.ShelfRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class NoteQueryService {

    private final NoteRepository noteRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final ShelfRepository shelfRepository;

    public List<MyNoteResponse> getMyNotes(
            final Long memberId,
            final Long bookId
    ) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 회원을 찾을 수 없습니다"));

        if (bookId == null) {
            return getMyNotesAll(member);
        }
        return getMyNotesByBookId(member, bookId);
    }

    private List<MyNoteResponse> getMyNotesByBookId(
            final Member member,
            final Long bookId
    ) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 도서를 찾을 수 없습니다"));
        return noteRepository.findNotesByMemberAndBook(member, book).stream()
                .map(MyNoteResponse::new)
                .toList();
    }

    public MyNoteResponse getNoteById(
            final Long memberId,
            final Long noteId
    ) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 기록을 찾을 수 없습니다"));
        validateNoteMine(note, memberId);

        return new MyNoteResponse(note);
    }

    private List<MyNoteResponse> getMyNotesAll(final Member member) {
        return noteRepository.findNotesByMember(member).stream()
                .map(MyNoteResponse::new)
                .toList();
    }

    private void validateNoteMine(
            final Note note,
            final Long memberId
    ) {
        if (!note.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("자신의 기록만 조회 가능합니다");
        }
    }
}
