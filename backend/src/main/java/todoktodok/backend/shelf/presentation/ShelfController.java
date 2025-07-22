package todoktodok.backend.shelf.presentation;

import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.resolver.LoginMember;
import todoktodok.backend.shelf.application.dto.request.ShelfRequest;
import todoktodok.backend.shelf.application.service.command.ShelfCommandService;
import todoktodok.backend.shelf.application.service.query.ShelfQueryService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/shelves")
public class ShelfController {

    private final ShelfCommandService shelfCommandService;
    private final ShelfQueryService shelfQueryService;

    @Auth(value = Role.USER)
    @GetMapping
    public ResponseEntity<List<BookResponse>> getMyBooks(
            @LoginMember final Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(shelfQueryService.getMyBooks(memberId));
    }

    @Auth(value = Role.USER)
    @PostMapping
    public ResponseEntity<BookResponse> addBook(
            @LoginMember final Long memberId,
            @RequestBody @Valid final ShelfRequest shelfRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shelfCommandService.addBook(memberId, shelfRequest));
    }
}
