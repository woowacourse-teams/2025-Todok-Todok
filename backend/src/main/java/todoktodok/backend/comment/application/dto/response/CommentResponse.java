package todoktodok.backend.comment.application.dto.response;

import java.time.LocalDateTime;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.member.application.dto.response.MemberResponse;

public record CommentResponse(
        Long commentId,
        MemberResponse member,
        LocalDateTime createdAt,
        String content,
        int likeCount,
        int replyCount,
        boolean isLiked
) {

    public CommentResponse(
            final Comment comment,
            final int likeCount,
            final int replyCount,
            final boolean isLiked
    ) {
        this(
                comment.getId(),
                new MemberResponse(comment.getMember()),
                comment.getCreatedAt(),
                comment.getContent(),
                likeCount,
                replyCount,
                isLiked
        );
    }
}
