package todoktodok.backend.discussion.presentation;

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
import todoktodok.backend.discussion.application.dto.request.DiscussionRequest;
import todoktodok.backend.discussion.application.service.command.DiscussionCommandService;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.resolver.LoginMember;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/discussions")
public class DiscussionController {

    private final DiscussionCommandService discussionCommandService;

    @Auth(value = Role.USER)
    @PostMapping
    public ResponseEntity<Void> createDiscussion(
            @LoginMember final Long memberId,
            @RequestBody @Valid final DiscussionRequest discussionRequest
    ) {
        Long discussionId = discussionCommandService.createDiscussion(memberId, discussionRequest);
        URI uri = createUri(discussionId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(uri)
                .build();
    }

    private URI createUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
