package todoktodok.backend.discussion.presentation;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import todoktodok.backend.discussion.application.dto.request.DiscussionRequest;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.discussion.application.service.command.DiscussionCommandService;
import todoktodok.backend.discussion.application.service.query.DiscussionQueryService;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.resolver.LoginMember;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/discussions")
public class DiscussionController {

    private final DiscussionCommandService discussionCommandService;
    private final DiscussionQueryService discussionQueryService;

    @Auth(value = Role.USER)
    @PostMapping
    public ResponseEntity<Void> createDiscussion(
            @LoginMember final Long memberId,
            @RequestBody @Valid final DiscussionRequest discussionRequest
    ) {
        final Long discussionId = discussionCommandService.createDiscussion(memberId, discussionRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(createUri(discussionId))
                .build();
    }

    @Auth(Role.USER)
    @GetMapping
    public ResponseEntity<List<DiscussionResponse>> getDiscussions(
            @LoginMember final Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(discussionQueryService.getDiscussions(memberId));
    }

    private URI createUri(final Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
