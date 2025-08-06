package todoktodok.backend.discussion.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import todoktodok.backend.comment.application.dto.request.CommentRequest;
import todoktodok.backend.discussion.application.dto.request.DiscussionRequest;
import todoktodok.backend.discussion.application.dto.request.DiscussionUpdateRequest;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.discussion.application.service.command.DiscussionCommandService;
import todoktodok.backend.discussion.application.service.query.DiscussionQueryService;
import todoktodok.backend.discussion.domain.DiscussionFilterType;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.resolver.LoginMember;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/discussions")
public class DiscussionController {

    private final DiscussionCommandService discussionCommandService;
    private final DiscussionQueryService discussionQueryService;

    @Operation(summary = "토론방 생성 API")
    @Auth(value = Role.USER)
    @PostMapping
    public ResponseEntity<Void> createDiscussion(
            @Parameter(hidden = true) @LoginMember final Long memberId,
            @RequestBody @Valid final DiscussionRequest discussionRequest
    ) {
        final Long discussionId = discussionCommandService.createDiscussion(memberId, discussionRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(createUri(discussionId))
                .build();
    }

    @Operation(summary = "토론방 단일 조회 API")
    @Auth(Role.USER)
    @GetMapping("/{discussionId}")
    public ResponseEntity<DiscussionResponse> getDiscussion(
            @Parameter(hidden = true) @LoginMember final Long memberId,
            @PathVariable final Long discussionId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(discussionQueryService.getDiscussion(memberId, discussionId));
    }

    @Operation(summary = "토론방 신고 API")
    @Auth(Role.USER)
    @PostMapping("/{discussionId}/report")
    public ResponseEntity<Void> report(
            @Parameter(hidden = true) @LoginMember final Long memberId,
            @PathVariable final Long discussionId
    ) {
        discussionCommandService.report(memberId, discussionId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @Operation(summary = "토론방 필터링 조회 API")
    @Auth(value = Role.USER)
    @GetMapping
    public ResponseEntity<List<DiscussionResponse>> getDiscussionsByKeywordAndType(
            @Parameter(hidden = true) @LoginMember final Long memberId,
            @RequestParam(required = false) final String keyword,
            @RequestParam final DiscussionFilterType type
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(discussionQueryService.getDiscussionsByKeywordAndType(memberId, keyword, type));
    }

    @Operation(summary = "토론방 수정 API")
    @Auth(value = Role.USER)
    @PatchMapping("/{discussionId}")
    public ResponseEntity<Void> updateDiscussion(
            @Parameter(hidden = true) @LoginMember final Long memberId,
            @PathVariable final Long discussionId,
            @RequestBody @Valid final DiscussionUpdateRequest discussionUpdateRequest
    ) {
        discussionCommandService.updateDiscussion(memberId, discussionId, discussionUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .location(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri())
                .build();
    }

    @Operation(summary = "토론방 삭제 API")
    @Auth(value = Role.USER)
    @DeleteMapping("/{discussionId}")
    public ResponseEntity<Void> deleteDiscussion(
            @Parameter(hidden = true) @LoginMember final Long memberId,
            @PathVariable final Long discussionId
    ) {
        discussionCommandService.deleteDiscussion(memberId, discussionId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private URI createUri(final Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
