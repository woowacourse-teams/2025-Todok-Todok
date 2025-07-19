package todoktodok.backend.book.presentation;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.book.application.service.query.BookQueryService;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.resolver.LoginMember;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookQueryService bookQueryService;

    @Auth(value = Role.USER)
    @GetMapping("/mine")
    public ResponseEntity<List<BookResponse>> getMyBooks(
            @LoginMember final Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookQueryService.getMyBooks(memberId));
    }
}
