package todoktodok.backend.shelf.presentation;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.resolver.LoginMember;
import todoktodok.backend.shelf.application.service.query.ShelfQueryService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/shelves")
public class ShelfController {

    private final ShelfQueryService shelfQueryService;

    @Auth(value = Role.USER)
    @GetMapping
    public ResponseEntity<List<BookResponse>> getMyBooks(
            @LoginMember final Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(shelfQueryService.getMyBooks(memberId));
    }
}
