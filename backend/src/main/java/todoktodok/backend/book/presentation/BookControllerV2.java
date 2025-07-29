package todoktodok.backend.book.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todoktodok.backend.book.application.dto.request.BookRequest;
import todoktodok.backend.book.application.service.command.BookCommandService;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.resolver.LoginMember;

@Tag(name = "book-controller")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v2/books")
public class BookControllerV2 {

    private final BookCommandService bookCommandService;

    @Operation(summary = "도서 생성 API")
    @Auth(value = Role.USER)
    @PostMapping
    public ResponseEntity<Long> createBook(
            @Parameter(hidden = true) @LoginMember final Long memberId,
            @RequestBody @Valid final BookRequest bookRequest
    ) {
        Long bookId = bookCommandService.createBook(memberId, bookRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookId);
    }
}
