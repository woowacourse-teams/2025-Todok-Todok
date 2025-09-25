package todoktodok.backend.discussion.application.dto.response;

import java.time.LocalDateTime;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.application.dto.response.MemberResponse;

public record ActiveDiscussionResponse(
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

    public ActiveDiscussionResponse(
            final Discussion discussion,
            final Long likeCount,
            final Long commentCount,
            final boolean isLikedByMe,
            final LocalDateTime lastCommentedAt
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
