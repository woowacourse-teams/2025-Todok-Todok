package todoktodok.backend.member.presentation;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.member.application.dto.request.BypassLoginRequest;
import todoktodok.backend.member.application.dto.response.RefreshTokenResponse;
import todoktodok.backend.member.application.dto.response.TokenResponse;
import todoktodok.backend.member.application.service.command.MemberCommandService;

@RestController
@AllArgsConstructor
@Profile({"local", "dev"})
@RequestMapping("/api/v1/admin")
public class AdminController implements AdminApiDocs{

    private final MemberCommandService memberCommandService;

    @Auth(value = Role.GUEST)
    @PostMapping("/login")
    public ResponseEntity<RefreshTokenResponse> adminLogin(
            @RequestBody @Valid final BypassLoginRequest loginRequest
    ) {
        final TokenResponse tokenResponse = memberCommandService.bypassLogin(loginRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", tokenResponse.accessToken())
                .header("Cache-Control", "no-store")
                .header("Pragma", "no-cache")
                .body(new RefreshTokenResponse(tokenResponse.refreshToken()));
    }
}
