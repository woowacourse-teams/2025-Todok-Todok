package todoktodok.backend.note.application.service.command;

import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.book.domain.repository.BookRepository;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.note.application.dto.request.NoteRequest;
import todoktodok.backend.note.domain.Note;
import todoktodok.backend.note.domain.repository.NoteRepository;
import todoktodok.backend.shelf.domain.repository.ShelfRepository;

@Service
@Transactional
@AllArgsConstructor
public class NoteCommandService {

    private final NoteRepository noteRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final ShelfRepository shelfRepository;

    public void createNote(
            final Long memberId,
            final NoteRequest noteRequest
    ) {
        final Book book = findBook(noteRequest);
        final Member member = findMember(memberId);

        validateShelfBook(book, member);

        final Note note = Note.builder()
                .snap(noteRequest.snap())
                .memo(noteRequest.memo())
                .book(book)
                .member(member)
                .build();
        noteRepository.save(note);
    }

    private Book findBook(final NoteRequest noteRequest) {
        return bookRepository.findById(noteRequest.bookId())
                .orElseThrow(() -> new NoSuchElementException("해당하는 도서를 찾을 수 없습니다"));
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 회원을 찾을 수 없습니다"));
    }

    private void validateShelfBook(
            final Book book,
            final Member member
    ) {
        if (!shelfRepository.existsByBookAndMember(book, member)) {
            throw new IllegalArgumentException("서재 등록한 도서만 기록 가능합니다");
        }
    }
}
