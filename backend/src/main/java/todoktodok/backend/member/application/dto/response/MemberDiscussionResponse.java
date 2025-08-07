package todoktodok.backend.member.application.dto.response;

import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.discussion.domain.Discussion;

public record MemberDiscussionResponse(
        Long discussionId,
        BookResponse book,
        String discussionTitle,
        String discussionOpinion
) {

    public MemberDiscussionResponse(final Discussion discussion) {
        this(
                discussion.getId(),
                new BookResponse(discussion.getBook()),
                discussion.getTitle(),
                discussion.getContent()
        );
    }
}
