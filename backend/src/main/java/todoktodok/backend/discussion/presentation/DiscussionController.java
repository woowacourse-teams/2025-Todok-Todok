package todoktodok.backend.discussion.presentation;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import todoktodok.backend.discussion.application.dto.request.DiscussionReportRequest;
import todoktodok.backend.discussion.application.dto.request.DiscussionRequest;
import todoktodok.backend.discussion.application.dto.request.DiscussionUpdateRequest;
import todoktodok.backend.discussion.application.dto.response.ActiveDiscussionPageResponse;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.discussion.application.dto.response.LatestDiscussionPageResponse;
import todoktodok.backend.discussion.application.service.command.DiscussionCommandService;
import todoktodok.backend.discussion.application.service.query.DiscussionQueryService;
import todoktodok.backend.discussion.domain.DiscussionFilterType;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.resolver.LoginMember;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/discussions")
public class DiscussionController implements DiscussionApiDocs {

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
    @PostMapping("/{discussionId}/report")
    public ResponseEntity<Void> report(
            @LoginMember final Long memberId,
            @PathVariable final Long discussionId,
            @RequestBody final DiscussionReportRequest discussionReportRequest
    ) {
        discussionCommandService.report(memberId, discussionId, discussionReportRequest.reason());

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @Auth(Role.USER)
    @GetMapping("/{discussionId}")
    public ResponseEntity<DiscussionResponse> getDiscussion(
            @LoginMember final Long memberId,
            @PathVariable final Long discussionId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(discussionQueryService.getDiscussion(memberId, discussionId));
    }

    @Auth(value = Role.USER)
    @GetMapping
    public ResponseEntity<LatestDiscussionPageResponse> getDiscussions(
            @LoginMember final Long memberId,
            @RequestParam final int size,
            @RequestParam(required = false) final String cursor
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(discussionQueryService.getDiscussions(memberId, size, cursor));
    }

    @Auth(value = Role.USER)
    @GetMapping("/search")
    public ResponseEntity<List<DiscussionResponse>> getDiscussionsByKeywordAndType(
            @LoginMember final Long memberId,
            @RequestParam(required = false) final String keyword,
            @RequestParam final DiscussionFilterType type
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(discussionQueryService.getDiscussionsByKeywordAndType(memberId, keyword, type));
    }

    @Auth(value = Role.USER)
    @GetMapping("/hot")
    public ResponseEntity<List<DiscussionResponse>> getHotDiscussions(
            @LoginMember final Long memberId,
            @RequestParam final int period,
            @RequestParam final int count
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(discussionQueryService.getHotDiscussions(memberId, period, count));
    }

    @Auth(value = Role.USER)
    @GetMapping("/active")
    public ResponseEntity<ActiveDiscussionPageResponse> getActiveDiscussions(
            @LoginMember final Long memberId,
            @RequestParam final int period,
            @RequestParam(defaultValue = "10") final int size,
            @RequestParam(required = false) final String cursor
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(discussionQueryService.getActiveDiscussions(memberId, period, size, cursor));
    }

    @Auth(value = Role.USER)
    @PatchMapping("/{discussionId}")
    public ResponseEntity<Void> updateDiscussion(
            @LoginMember final Long memberId,
            @PathVariable final Long discussionId,
            @RequestBody @Valid final DiscussionUpdateRequest discussionUpdateRequest
    ) {
        discussionCommandService.updateDiscussion(memberId, discussionId, discussionUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .location(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri())
                .build();
    }

    @Auth(value = Role.USER)
    @DeleteMapping("/{discussionId}")
    public ResponseEntity<Void> deleteDiscussion(
            @LoginMember final Long memberId,
            @PathVariable final Long discussionId
    ) {
        discussionCommandService.deleteDiscussion(memberId, discussionId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Auth(value = Role.USER)
    @PostMapping("/{discussionId}/like")
    public ResponseEntity<Void> toggleLike(
            @LoginMember final Long memberId,
            @PathVariable final Long discussionId
    ) {
        final boolean isLiked = discussionCommandService.toggleLike(memberId, discussionId);

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
