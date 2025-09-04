package todoktodok.backend.discussion.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DiscussionUpdateRequest(
        @NotBlank(message = "토론방 제목을 입력해주세요")
        @Size(max = 50, message = "토론방 제목은 1자 이상, 50자 이하여야 합니다")
        String discussionTitle,

        @NotBlank(message = "토론방 내용을 입력해주세요")
        @Size(max = 2500, message = "토론방 내용은 1자 이상, 2500자 이하여야 합니다")
        String discussionOpinion
) {
}
