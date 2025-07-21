package todoktodok.backend.shelf.application.dto.request;

import jakarta.validation.constraints.NotNull;

public record ShelfRequest(
        @NotNull(message = "bookId를 입력해주세요")
        Long bookId
) {
}
