package todoktodok.backend.reply.presentation;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.resolver.LoginMember;
import todoktodok.backend.reply.application.dto.request.ReplyRequest;
import todoktodok.backend.reply.application.dto.response.ReplyResponse;
import todoktodok.backend.reply.application.service.command.ReplyCommandService;
import todoktodok.backend.reply.application.service.query.ReplyQueryService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/discussions/{discussionId}/comments/{commentId}/replies")
public class ReplyController implements ReplyApiDocs {

    private final ReplyCommandService replyCommandService;
    private final ReplyQueryService replyQueryService;

    @Auth(value = Role.USER)
    @PostMapping
    public ResponseEntity<Void> createReply(
            @LoginMember final Long memberId,
            @PathVariable final Long discussionId,
            @PathVariable final Long commentId,
            @RequestBody @Valid final ReplyRequest replyRequest
    ) {
        final Long replyId = replyCommandService.createReply(memberId, discussionId, commentId, replyRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(createUri(replyId))
                .build();
    }

    @Auth(value = Role.USER)
    @PostMapping("/{replyId}/report")
    public ResponseEntity<Void> report(
            @LoginMember final Long memberId,
            @PathVariable final Long discussionId,
            @PathVariable final Long commentId,
            @PathVariable final Long replyId
    ) {
        replyCommandService.report(memberId, discussionId, commentId, replyId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @Auth(value = Role.USER)
    @GetMapping
    public ResponseEntity<List<ReplyResponse>> getReplies(
            @LoginMember final Long memberId,
            @PathVariable final Long discussionId,
            @PathVariable final Long commentId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(replyQueryService.getReplies(memberId, discussionId, commentId));
    }

    @Auth(value = Role.USER)
    @PatchMapping("/{replyId}")
    public ResponseEntity<Void> updateReply(
            @LoginMember final Long memberId,
            @PathVariable final Long discussionId,
            @PathVariable final Long commentId,
            @PathVariable final Long replyId,
            @RequestBody @Valid final ReplyRequest replyRequest
    ) {
        replyCommandService.updateReply(memberId, discussionId, commentId, replyId, replyRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .location(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri())
                .build();
    }

    @Auth(value = Role.USER)
    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(
            @LoginMember final Long memberId,
            @PathVariable final Long discussionId,
            @PathVariable final Long commentId,
            @PathVariable final Long replyId
    ) {
        replyCommandService.deleteReply(memberId, discussionId, commentId, replyId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Auth(value = Role.USER)
    @PostMapping("/{replyId}/like")
    public ResponseEntity<Void> toggleLike(
            @LoginMember final Long memberId,
            @PathVariable final Long discussionId,
            @PathVariable final Long commentId,
            @PathVariable final Long replyId
    ) {
        final boolean isLiked = replyCommandService.toggleLike(memberId, discussionId, commentId, replyId);

        if (isLiked) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    private URI createUri(final Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
