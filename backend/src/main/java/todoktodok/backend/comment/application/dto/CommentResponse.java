package todoktodok.backend.comment.application.dto;

import java.time.LocalDateTime;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.member.application.dto.response.MemberResponse;

public record CommentResponse(
        Long commentId,
        MemberResponse member,
        LocalDateTime createdAt,
        String content
) {

    public CommentResponse(final Comment comment){
        this(
                comment.getId(),
                new MemberResponse(comment.getMember()),
                comment.getCreatedAt(),
                comment.getContent()
        );
    }
}
