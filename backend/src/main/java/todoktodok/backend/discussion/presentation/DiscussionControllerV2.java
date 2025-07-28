package todoktodok.backend.discussion.presentation;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import todoktodok.backend.discussion.application.dto.request.DiscussionRequestV2;
import todoktodok.backend.discussion.application.service.command.DiscussionCommandService;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.resolver.LoginMember;

@Tag(name = "discussion-controller")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v2/discussions")
public class DiscussionControllerV2 {

    private final DiscussionCommandService discussionCommandService;

    @Operation(summary = "토론방 생성 API")
    @Auth(value = Role.USER)
    @PostMapping
    public ResponseEntity<Void> createDiscussion(
            @Parameter(hidden = true) @LoginMember final Long memberId,
            @RequestBody @Valid final DiscussionRequestV2 discussionRequestV2
    ) {
        final Long discussionId = discussionCommandService.createDiscussionV2(memberId, discussionRequestV2);

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create("/api/v1/discussions/" + discussionId))
                .build();
    }
}
