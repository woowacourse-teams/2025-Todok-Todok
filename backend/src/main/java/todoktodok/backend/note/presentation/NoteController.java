package todoktodok.backend.note.presentation;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.resolver.LoginMember;
import todoktodok.backend.note.application.dto.request.NoteRequest;
import todoktodok.backend.note.application.service.command.NoteCommandService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/notes")
public class NoteController {

    private final NoteCommandService noteCommandService;

    @Auth(value = Role.USER)
    @PostMapping
    public ResponseEntity<Void> createNote(
            @LoginMember final Long memberId,
            @RequestBody @Valid final NoteRequest noteRequest
    ) {
        noteCommandService.createNote(noteRequest, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
