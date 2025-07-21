package todoktodok.backend.member.presentation;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.resolver.LoginMember;
import todoktodok.backend.member.application.dto.request.LoginRequest;
import todoktodok.backend.member.application.dto.request.SignupRequest;
import todoktodok.backend.member.application.service.command.MemberCommandService;
import todoktodok.backend.global.auth.Role;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberCommandService memberCommandService;

    @Auth(value = Role.GUEST)
    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody @Valid final LoginRequest loginRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", memberCommandService.login(loginRequest))
                .build();
    }

    @Auth(value = Role.TEMP_USER)
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(
            @RequestBody @Valid final SignupRequest signupRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Authorization", memberCommandService.signup(signupRequest))
                .build();
    }

    @Auth(value = Role.USER)
    @PostMapping("/{id}/block")
    public ResponseEntity<Void> block(
            @LoginMember final Long memberId,
            @PathVariable("id") final Long targetId
    ) {
        memberCommandService.block(memberId, targetId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @Auth(value = Role.USER)
    @PostMapping("/{id}/report")
    public ResponseEntity<Void> report(
            @LoginMember final Long memberId,
            @PathVariable("id") final Long targetId
    ) {
        memberCommandService.report(memberId, targetId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
