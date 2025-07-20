package todoktodok.backend.note.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.note.domain.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findNotesByMember(final Member member);
}
