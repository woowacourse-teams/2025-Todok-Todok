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
        URI uri = URI.create(
                "/api/v1/discussions/" +
                        discussionCommandService.createDiscussion(memberId, discussionRequest)
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(uri)
                .build();
    }
}
