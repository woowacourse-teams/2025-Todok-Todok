package todoktodok.backend.note.application.dto.response;

import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.note.domain.Note;

public record MyNoteResponse(
        Long noteId,
        BookResponse book,
        String snap,
        String memo
) {

    public MyNoteResponse(final Note note) {
        this(
                note.getId(),
                new BookResponse(note.getBook()),
                note.getSnap(),
                note.getMemo()
        );
    }
}
