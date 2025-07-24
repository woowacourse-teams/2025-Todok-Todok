package todoktodok.backend.note.presentation;

import io.swagger.v3.oas.annotations.Operation;
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
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.resolver.LoginMember;
import todoktodok.backend.note.application.dto.request.NoteRequest;
import todoktodok.backend.note.application.dto.response.MyNoteResponse;
import todoktodok.backend.note.application.service.command.NoteCommandService;
import todoktodok.backend.note.application.service.query.NoteQueryService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/notes")
public class NoteController {

    private final NoteCommandService noteCommandService;
    private final NoteQueryService noteQueryService;

    @Operation(summary = "기록 생성 API")
    @Auth(value = Role.USER)
    @PostMapping
    public ResponseEntity<Void> createNote(
            @LoginMember final Long memberId,
            @RequestBody @Valid final NoteRequest noteRequest
    ) {
        noteCommandService.createNote(memberId, noteRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @Operation(summary = "도서별 내 기록 조회 API")
    @Auth(value = Role.USER)
    @GetMapping("/mine")
    public ResponseEntity<List<MyNoteResponse>> getMyNotes(
            @LoginMember final Long memberId,
            @RequestParam(value = "bookId", required = false) final Long bookId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(noteQueryService.getMyNotes(memberId, bookId));
    }

    @Operation(summary = "기록 단일 조회 API")
    @Auth(value = Role.USER)
    @GetMapping("/{noteId}")
    public ResponseEntity<MyNoteResponse> getMyNote(
            @LoginMember final Long memberId,
            @PathVariable final Long noteId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(noteQueryService.getMyNote(memberId, noteId));
    }
}
