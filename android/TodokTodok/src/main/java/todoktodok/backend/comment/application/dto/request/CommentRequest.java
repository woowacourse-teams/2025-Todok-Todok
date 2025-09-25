package todoktodok.backend.comment.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank(message = "댓글 내용을 입력해주세요")
        @Size(max = 1500, message = "댓글 내용은 1자 이상, 1500자 이하여야 합니다")
        String content
) {
}
