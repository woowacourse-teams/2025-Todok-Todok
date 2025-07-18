package todoktodok.backend.member.presentation;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todoktodok.backend.member.application.dto.request.LoginRequest;
import todoktodok.backend.member.application.service.command.MemberCommandService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberCommandService memberCommandService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody @Valid final LoginRequest loginRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", "Bearer " + memberCommandService.login(loginRequest))
                .build();
    }
}
