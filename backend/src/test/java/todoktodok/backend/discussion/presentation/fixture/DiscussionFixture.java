package todoktodok.backend.discussion.presentation.fixture;

import todoktodok.backend.book.domain.Book;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.domain.Member;

public class DiscussionFixture {

    public static Discussion create(
            final String title,
            final String content,
            final Member member,
            final Book book
    ) {
        return Discussion.builder()
                .title(title)
                .content(content)
                .member(member)
                .book(book)
                .build();
    }
}
