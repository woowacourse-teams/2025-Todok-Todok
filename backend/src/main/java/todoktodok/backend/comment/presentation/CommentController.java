package todoktodok.backend.comment.presentation;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import todoktodok.backend.comment.application.dto.CommentResponse;
import todoktodok.backend.comment.application.dto.request.CommentRequest;
import todoktodok.backend.comment.application.service.command.CommentCommandService;
import todoktodok.backend.comment.application.service.query.CommentQueryService;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.resolver.LoginMember;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/discussions/{discussionId}/comments")
public class CommentController {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;

    @Auth(value = Role.USER)
    @PostMapping
    public ResponseEntity<Void> createComment(
            @LoginMember final Long memberId,
            @PathVariable final Long discussionId,
            @RequestBody @Valid final CommentRequest commentRequest
    ) {
        final Long commentId = commentCommandService.createComment(memberId, discussionId, commentRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(createUri(commentId))
                .build();
    }

    @Auth(value = Role.USER)
    @PostMapping("/{commentId}/report")
    public ResponseEntity<Void> report(
            @LoginMember final Long memberId,
            @PathVariable final Long discussionId,
            @PathVariable final Long commentId
    ) {
        commentCommandService.report(memberId, discussionId, commentId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @Auth(value = Role.USER)
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(
            @LoginMember final Long memberId,
            @PathVariable final Long discussionId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentQueryService.getComments(memberId, discussionId));
    }

    private URI createUri(final Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
