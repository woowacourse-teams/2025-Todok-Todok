package todoktodok.backend.discussion.application.dto.response;

import java.time.LocalDateTime;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.application.dto.response.MemberResponse;
import todoktodok.backend.note.application.dto.response.MyNoteResponse;

public record DiscussionResponse(
        Long discussionId,
        BookResponse book,
        MemberResponse member,
        LocalDateTime createdAt,
        MyNoteResponse note,
        String discussionTitle,
        String discussionOpinion
) {
    public DiscussionResponse(final Discussion discussion) {
        this(
                discussion.getId(),
                new BookResponse(discussion.getBook()),
                new MemberResponse(discussion.getMember()),
                discussion.getCreatedAt(),
                discussion.getNote() != null ? new MyNoteResponse(discussion.getNote()) : null,
                discussion.getTitle(),
                discussion.getContent()
        );
    }
}
