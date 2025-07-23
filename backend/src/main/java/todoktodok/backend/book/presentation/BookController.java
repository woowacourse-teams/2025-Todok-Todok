package todoktodok.backend.book.presentation;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.book.application.service.query.BookQueryService;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookQueryService bookQueryService;

    @Operation(summary = "도서 검색 API")
    @Auth(value = Role.USER)
    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> search(
            @RequestParam(value = "keyword", required = false) final String keyword
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookQueryService.search(keyword));
    }
}
