package todoktodok.backend.discussion.application.dto.response;

import java.time.LocalDateTime;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.application.dto.response.MemberResponse;

public record DiscussionResponse(
        Long discussionId,
        BookResponse book,
        MemberResponse member,
        LocalDateTime createdAt,
        String discussionTitle,
        String discussionOpinion,
        int viewCount,
        int likeCount,
        int commentCount,
        boolean isLikedByMe
) {
    public DiscussionResponse(
            final Discussion discussion,
            final int likeCount,
            final int commentCount,
            final boolean isLikedByMe
    ) {
        this(
                discussion.getId(),
                new BookResponse(discussion.getBook()),
                new MemberResponse(discussion.getMember()),
                discussion.getCreatedAt(),
                discussion.getTitle(),
                discussion.getContent(),
                Math.toIntExact(discussion.getViewCount()),
                likeCount,
                commentCount,
                isLikedByMe
        );
    }
}
