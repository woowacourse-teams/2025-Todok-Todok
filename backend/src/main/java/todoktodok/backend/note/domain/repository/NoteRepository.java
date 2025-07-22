package todoktodok.backend.note.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.note.domain.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findNotesByMember(final Member member);

    List<Note> findNotesByMemberAndBook(final Member member, final Book book);
}
