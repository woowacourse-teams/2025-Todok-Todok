package todoktodok.backend.discussion.application.dto.response;

import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.discussion.domain.Discussion;

public record DiscussionResponse(
        Long discussionId,
        BookResponse bookResponse,
        String discussionTitle,
        String discussionOpinion
) {
    public DiscussionResponse(Discussion discussion) {
        this(
                discussion.getId(),
                new BookResponse(discussion.getBook()),
                discussion.getTitle(),
                discussion.getContent()
        );
    }
}
