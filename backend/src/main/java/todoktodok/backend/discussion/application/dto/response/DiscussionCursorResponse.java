package todoktodok.backend.discussion.application.dto.response;

import java.time.LocalDateTime;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.application.dto.response.MemberResponse;

public record DiscussionCursorResponse(
        Long discussionId,
        BookResponse book,
        MemberResponse member,
        LocalDateTime createdAt,
        String discussionTitle,
        String discussionOpinion,
        int likeCount,
        int commentCount,
        boolean isLikedByMe,
        LocalDateTime lastCommentedAt
) {

    public DiscussionCursorResponse(
            Discussion discussion,
            Long likeCount,
            Long commentCount,
            boolean isLikedByMe,
            LocalDateTime lastCommentedAt
    ) {
        this(
                discussion.getId(),
                new BookResponse(discussion.getBook()),
                new MemberResponse(discussion.getMember()),
                discussion.getCreatedAt(),
                discussion.getTitle(),
                discussion.getContent(),
                likeCount.intValue(),
                commentCount.intValue(),
                isLikedByMe,
                lastCommentedAt
        );
    }
}
