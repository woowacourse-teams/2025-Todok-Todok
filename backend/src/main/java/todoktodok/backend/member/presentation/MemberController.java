package todoktodok.backend.member.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.resolver.LoginMember;
import todoktodok.backend.global.resolver.TempMember;
import todoktodok.backend.member.application.dto.request.LoginRequest;
import todoktodok.backend.member.application.dto.request.ProfileUpdateRequest;
import todoktodok.backend.member.application.dto.request.SignupRequest;
import todoktodok.backend.member.application.dto.response.ProfileResponse;
import todoktodok.backend.member.application.dto.response.ProfileUpdateResponse;
import todoktodok.backend.member.application.service.command.MemberCommandService;
import todoktodok.backend.member.application.service.query.MemberQueryService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    @Operation(summary = "로그인 API")
    @Auth(value = Role.GUEST)
    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody @Valid final LoginRequest loginRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", memberCommandService.login(loginRequest))
                .build();
    }

    @Operation(summary = "회원가입 API")
    @Auth(value = Role.TEMP_USER)
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(
            @TempMember final String memberEmail,
            @RequestBody @Valid final SignupRequest signupRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Authorization", memberCommandService.signup(signupRequest, memberEmail))
                .build();
    }

    @Operation(summary = "작성자 차단 API")
    @Auth(value = Role.USER)
    @PostMapping("/{memberId}/block")
    public ResponseEntity<Void> block(
            @Parameter(hidden = true) @LoginMember final Long memberId,
            @PathVariable("memberId") final Long targetId
    ) {
        memberCommandService.block(memberId, targetId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @Operation(summary = "작성자 신고 API")
    @Auth(value = Role.USER)
    @PostMapping("/{memberId}/report")
    public ResponseEntity<Void> report(
            @Parameter(hidden = true) @LoginMember final Long memberId,
            @PathVariable("memberId") final Long targetId
    ) {
        memberCommandService.report(memberId, targetId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @Operation(summary = "프로필 정보 조회 API")
    @Auth(value = Role.USER)
    @GetMapping("/{memberId}/profile")
    public ResponseEntity<ProfileResponse> getProfile(
            @PathVariable final Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberQueryService.getProfile(memberId));
    }

    @Operation(summary = "프로필 정보 수정 API")
    @Auth(value = Role.USER)
    @PutMapping("/profile")
    public ResponseEntity<ProfileUpdateResponse> updateProfile(
            @Parameter(hidden = true) @LoginMember final Long memberId,
            @RequestBody @Valid final ProfileUpdateRequest profileUpdateRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberCommandService.updateProfile(memberId, profileUpdateRequest));
    }
}
