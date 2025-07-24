package todoktodok.backend.discussion.application.dto.response;

import java.time.LocalDateTime;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.application.dto.response.MemberResponse;
import todoktodok.backend.note.application.dto.response.MyNoteResponse;

public record DiscussionResponse(
        Long discussionId,
        BookResponse bookResponse,
        MemberResponse memberResponse,
        LocalDateTime createdAt,
        MyNoteResponse noteResponse,
        String discussionTitle,
        String discussionOpinion
) {
    public DiscussionResponse(final Discussion discussion) {
        this(
                discussion.getId(),
                new BookResponse(discussion.getBook()),
                new MemberResponse(discussion.getMember()),
                discussion.getCreatedAt(),
                new MyNoteResponse(discussion.getNote()),
                discussion.getTitle(),
                discussion.getContent()
        );
    }
}
