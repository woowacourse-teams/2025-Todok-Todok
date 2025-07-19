package todoktodok.backend.note.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NoteRequest(
//        @NotNull(message = "bookId를 입력해주세요")
//        Long bookId,

        @Size(max = 1000, message = "스냅은 1000자 이하로 입력해주세요")
        String snap,

        @Size(max = 1000, message = "메모는 1000자 이하로 입력해주세요")
        String memo
) {
}
