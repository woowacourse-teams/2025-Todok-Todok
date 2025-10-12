package todoktodok.backend.book.presentation;

import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import todoktodok.backend.book.application.dto.request.BookRequest;
import todoktodok.backend.book.application.dto.response.AladinBookResponse;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.book.application.dto.response.LatestAladinBookPageResponse;
import todoktodok.backend.book.application.service.command.BookCommandService;
import todoktodok.backend.book.application.service.query.BookQueryService;
import todoktodok.backend.discussion.application.dto.response.LatestDiscussionPageResponse;
import todoktodok.backend.discussion.application.service.query.DiscussionQueryService;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.resolver.LoginMember;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController implements BookApiDocs {

    private final BookCommandService bookCommandService;
    private final BookQueryService bookQueryService;
    private final DiscussionQueryService discussionQueryService;

    @Auth(value = Role.USER)
    @PostMapping
    public ResponseEntity<Long> createBook(
            @LoginMember final Long memberId,
            @RequestBody @Valid final BookRequest bookRequest
    ) {
        final Long bookId = bookCommandService.createOrUpdateBook(memberId, bookRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookId);
    }

    @Auth(value = Role.USER)
    @GetMapping("/search")
    public ResponseEntity<List<AladinBookResponse>> search(
            @RequestParam(required = false) final String keyword
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookQueryService.search(keyword));
    }

    @Auth(value = Role.USER)
    @GetMapping("/searchByPaging")
    public ResponseEntity<LatestAladinBookPageResponse> searchByPaging(
            @RequestParam final int size,
            @RequestParam(required = false) final String cursor,
            @RequestParam(required = false) final String keyword
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookQueryService.searchByPaging(size, cursor, keyword));
    }

    @Auth(value = Role.USER)
    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponse> getBook(
            @PathVariable final Long bookId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookQueryService.getBook(bookId));
    }

    @Auth(value = Role.USER)
    @GetMapping("/{bookId}/discussions")
    public ResponseEntity<LatestDiscussionPageResponse> getDiscussionsByBook(
            @LoginMember final Long memberId,
            @PathVariable final Long bookId,
            @RequestParam final int size,
            @RequestParam(required = false) final String cursor
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(discussionQueryService.getDiscussionsByBook(memberId, bookId, size, cursor));
    }
}
