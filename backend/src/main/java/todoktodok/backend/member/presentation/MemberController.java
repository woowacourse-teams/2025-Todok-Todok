package todoktodok.backend.member.presentation;

import jakarta.validation.Valid;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.resolver.LoginMember;
import todoktodok.backend.global.resolver.TempMember;
import todoktodok.backend.member.application.dto.request.*;
import todoktodok.backend.member.application.dto.response.BlockMemberResponse;
import todoktodok.backend.member.application.dto.response.ProfileImageUpdateResponse;
import todoktodok.backend.member.application.dto.response.ProfileResponse;
import todoktodok.backend.member.application.dto.response.ProfileUpdateResponse;
import todoktodok.backend.member.application.dto.response.RefreshTokenResponse;
import todoktodok.backend.member.application.dto.response.TokenResponse;
import todoktodok.backend.member.application.service.command.MemberCommandService;
import todoktodok.backend.member.application.service.query.MemberQueryService;
import todoktodok.backend.member.domain.MemberDiscussionFilterType;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController implements MemberApiDocs {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    @Auth(value = Role.GUEST)
    @PostMapping("/login")
    public ResponseEntity<RefreshTokenResponse> login(
            @RequestBody @Valid final LoginRequest loginRequest
    ) {
        final TokenResponse tokenResponse = memberCommandService.login(loginRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", tokenResponse.accessToken())
                .header("Cache-Control", "no-store")
                .header("Pragma", "no-cache")
                .body(new RefreshTokenResponse(tokenResponse.refreshToken()));
    }

    @Deprecated
    @Auth(value = Role.GUEST)
    @PostMapping("/login/legacy")
    public ResponseEntity<RefreshTokenResponse> loginLegacy(
            @RequestBody @Valid final LoginRequestLegacy loginRequest
    ) {
        final TokenResponse tokenResponse = memberCommandService.loginLegacy(loginRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", tokenResponse.accessToken())
                .header("Cache-Control", "no-store")
                .header("Pragma", "no-cache")
                .body(new RefreshTokenResponse(tokenResponse.refreshToken()));
    }

    @Auth(value = Role.TEMP_USER)
    @PostMapping("/signup")
    public ResponseEntity<RefreshTokenResponse> signup(
            @TempMember final String memberEmail,
            @RequestBody @Valid final SignupRequest signupRequest
    ) {
        final TokenResponse tokenResponse = memberCommandService.signup(signupRequest, memberEmail);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Authorization", tokenResponse.accessToken())
                .header("Cache-Control", "no-store")
                .header("Pragma", "no-cache")
                .body(new RefreshTokenResponse(tokenResponse.refreshToken()));
    }

    @Deprecated
    @Auth(value = Role.TEMP_USER)
    @PostMapping("/signup/legacy")
    public ResponseEntity<RefreshTokenResponse> signupLegacy(
            @TempMember final String memberEmail,
            @RequestBody @Valid final SignupRequestLegacy signupRequest
    ) {
        final TokenResponse tokenResponse = memberCommandService.signupLegacy(signupRequest, memberEmail);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Authorization", tokenResponse.accessToken())
                .header("Cache-Control", "no-store")
                .header("Pragma", "no-cache")
                .body(new RefreshTokenResponse(tokenResponse.refreshToken()));
    }

    @Auth(value = Role.EXPIRED_USER)
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(
            @RequestBody @Valid final RefreshTokenRequest refreshTokenRequest
    ) {
        final TokenResponse tokenResponse = memberCommandService.refresh(refreshTokenRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", tokenResponse.accessToken())
                .header("Cache-Control", "no-store")
                .header("Pragma", "no-cache")
                .body(new RefreshTokenResponse(tokenResponse.refreshToken()));
    }

    @Auth(value = Role.USER)
    @PostMapping("/{memberId}/block")
    public ResponseEntity<Void> block(
            @LoginMember final Long memberId,
            @PathVariable("memberId") final Long targetId
    ) {
        memberCommandService.block(memberId, targetId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @Auth(value = Role.USER)
    @PostMapping("/{memberId}/report")
    public ResponseEntity<Void> report(
            @LoginMember final Long memberId,
            @PathVariable("memberId") final Long targetId,
            @RequestBody final MemberReportRequest memberReportRequest
    ) {
        memberCommandService.report(memberId, targetId, memberReportRequest.reason());
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @Auth(value = Role.USER)
    @GetMapping("/{memberId}/profile")
    public ResponseEntity<ProfileResponse> getProfile(
            @PathVariable final Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberQueryService.getProfile(memberId));
    }

    @Auth(value = Role.USER)
    @GetMapping("/{memberId}/books")
    public ResponseEntity<List<BookResponse>> getActiveBooks(
            @PathVariable final Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberQueryService.getActiveBooks(memberId));
    }

    @Auth(value = Role.USER)
    @GetMapping("/{memberId}/discussions")
    public ResponseEntity<List<DiscussionResponse>> getMemberDiscussionsByType(
            @PathVariable final Long memberId,
            @RequestParam final MemberDiscussionFilterType type
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberQueryService.getMemberDiscussionsByType(memberId, type));
    }

    @Auth(value = Role.USER)
    @GetMapping("/block")
    public ResponseEntity<List<BlockMemberResponse>> getBlockMembers(
            @LoginMember final Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberQueryService.getBlockMembers(memberId));
    }

    @Auth(value = Role.USER)
    @PutMapping("/profile")
    public ResponseEntity<ProfileUpdateResponse> updateProfile(
            @LoginMember final Long memberId,
            @RequestBody @Valid final ProfileUpdateRequest profileUpdateRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberCommandService.updateProfile(memberId, profileUpdateRequest));
    }

    @Auth(value = Role.USER)
    @PatchMapping(value = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileImageUpdateResponse> updateProfileImage(
            @LoginMember final Long memberId,
            @RequestPart("profileImage") final MultipartFile profileImage
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberCommandService.updateProfileImage(memberId, profileImage));
    }

    @Auth(value = Role.USER)
    @DeleteMapping
    public ResponseEntity<Void> deleteMember(
            @LoginMember final Long memberId,
            @RequestBody @Valid final RefreshTokenRequest refreshTokenRequest
    ) {
        memberCommandService.deleteMember(memberId, refreshTokenRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Auth(value = Role.USER)
    @DeleteMapping("/{memberId}/block")
    public ResponseEntity<Void> deleteBlock(
            @LoginMember final Long memberId,
            @PathVariable("memberId") final Long targetId
    ) {
        memberCommandService.deleteBlock(memberId, targetId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
