package todoktodok.backend.book.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookRequest(
        @NotBlank(message = "도서 ISBN을 입력해주세요")
        @Size(min = 13, max = 13, message = "ISBN은 13자여야 합니다")
        String bookIsbn,

        @NotBlank(message = "도서 제목을 입력해주세요")
        String bookTitle,

        @NotBlank(message = "도서 저자를 입력해주세요")
        String bookAuthor,

        @NotBlank(message = "도서 이미지를 입력해주세요")
        String bookImage
) {
}
