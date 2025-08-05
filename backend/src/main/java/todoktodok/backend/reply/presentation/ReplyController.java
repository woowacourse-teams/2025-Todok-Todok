package todoktodok.backend.reply.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.resolver.LoginMember;
import todoktodok.backend.reply.application.dto.request.ReplyRequest;
import todoktodok.backend.reply.application.service.command.ReplyCommandService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/discussions/{discussionId}/comments/{commentId}/replies")
public class ReplyController {

    private final ReplyCommandService replyCommandService;

    @Operation(summary = "대댓글 생성 API")
    @Auth(value = Role.USER)
    @PostMapping
    public ResponseEntity<Void> createReply(
            @Parameter(hidden = true) @LoginMember final Long memberId,
            @PathVariable final Long discussionId,
            @PathVariable final Long commentId,
            @RequestBody @Valid final ReplyRequest replyRequest
    ) {
        final Long replyId = replyCommandService.createReply(memberId, discussionId, commentId, replyRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(createUri(replyId))
                .build();
    }

    private URI createUri(final Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
