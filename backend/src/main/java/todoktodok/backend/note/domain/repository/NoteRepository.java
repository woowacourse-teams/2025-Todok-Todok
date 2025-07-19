package todoktodok.backend.note.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.note.domain.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
