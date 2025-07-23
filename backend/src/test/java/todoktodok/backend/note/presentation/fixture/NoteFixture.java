package todoktodok.backend.note.presentation.fixture;

import todoktodok.backend.book.domain.Book;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.note.domain.Note;

public class NoteFixture {

    public static Note create(
            final String snap,
            final String memo,
            final Book book,
            final Member member
    ) {
        return Note.builder()
                .snap(snap)
                .memo(memo)
                .book(book)
                .member(member)
                .build();
    }
}
