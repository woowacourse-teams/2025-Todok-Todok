package todoktodok.backend.discussion.domain.fixture;

import todoktodok.backend.book.domain.Book;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.note.domain.Note;

public class DiscussionFixture {

    public static Discussion create(
            final String title,
            final String content,
            final Member member,
            final Book book,
            final Note note
    ) {
        return Discussion.builder()
                .title(title)
                .content(content)
                .member(member)
                .book(book)
                .note(note)
                .build();
    }
}
