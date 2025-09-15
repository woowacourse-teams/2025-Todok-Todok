package todoktodok.backend.member.application.service.command;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import todoktodok.backend.global.jwt.JwtTokenProvider;
import todoktodok.backend.global.jwt.TokenInfo;
import todoktodok.backend.member.application.ImageType;
import todoktodok.backend.member.application.dto.request.LoginRequest;
import todoktodok.backend.member.application.dto.request.ProfileUpdateRequest;
import todoktodok.backend.member.application.dto.request.RefreshTokenRequest;
import todoktodok.backend.member.application.dto.request.SignupRequest;
import todoktodok.backend.member.application.dto.response.ProfileImageUpdateResponse;
import todoktodok.backend.member.application.dto.response.ProfileUpdateResponse;
import todoktodok.backend.member.application.dto.response.TokenResponse;
import todoktodok.backend.member.domain.Block;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.MemberReport;
import todoktodok.backend.member.domain.MemberReportReason;
import todoktodok.backend.member.domain.RefreshToken;
import todoktodok.backend.member.domain.repository.BlockRepository;
import todoktodok.backend.member.domain.repository.MemberReportRepository;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.member.domain.repository.RefreshTokenRepository;
import todoktodok.backend.member.infrastructure.ProfileImageResponse;
import todoktodok.backend.member.infrastructure.S3ImageUploadClient;

@Service
@Transactional
@AllArgsConstructor
public class MemberCommandService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private final MemberRepository memberRepository;
    private final BlockRepository blockRepository;
    private final MemberReportRepository memberReportRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final S3ImageUploadClient s3ImageUploadClient;

    public TokenResponse login(final LoginRequest loginRequest) {
        final Optional<Member> memberOrEmpty = memberRepository.findByEmailAndDeletedAtIsNull(loginRequest.email());
        if (memberOrEmpty.isPresent()) {
            final String accessToken = jwtTokenProvider.createAccessToken(memberOrEmpty.get());
            final String refreshToken = jwtTokenProvider.createRefreshToken(memberOrEmpty.get());

            final RefreshToken savedRefreshToken = RefreshToken.create(refreshToken);
            saveRefreshTokenIfUnique(savedRefreshToken, memberOrEmpty.get().getId());

            return new TokenResponse(accessToken, refreshToken);
        }

        final String tempToken = jwtTokenProvider.createTempToken(loginRequest.email());
        return new TokenResponse(tempToken, null);
    }

    public TokenResponse signup(
            final SignupRequest signupRequest,
            final String memberEmail
    ) {
        validateDuplicatedNickname(signupRequest.nickname());
        validateDuplicatedEmail(signupRequest);
        validateEmailWithTokenEmail(signupRequest, memberEmail);

        final Member member = Member.builder()
                .nickname(signupRequest.nickname())
                .email(signupRequest.email())
                .profileImage(signupRequest.profileImage())
                .build();

        final Member savedMember = memberRepository.save(member);
        final String accessToken = jwtTokenProvider.createAccessToken(savedMember);
        final String refreshToken = jwtTokenProvider.createRefreshToken(savedMember);
        refreshTokenRepository.save(RefreshToken.create(refreshToken));

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refresh(final RefreshTokenRequest refreshTokenRequest) {
        final String oldRefreshTokenRequest = refreshTokenRequest.refreshToken();
        final TokenInfo tokenInfo = jwtTokenProvider.getInfoByRefreshToken(oldRefreshTokenRequest);
        final Long memberId = tokenInfo.id();
        final Member member = findMember(memberId);

        final String accessToken = jwtTokenProvider.createAccessToken(member);
        final RefreshToken oldRefreshToken = findRefreshToken(oldRefreshTokenRequest);

        refreshTokenRepository.delete(oldRefreshToken);

        final RefreshToken newRefreshToken = RefreshToken.create(jwtTokenProvider.createRefreshToken(member));

        saveRefreshTokenIfUnique(newRefreshToken, memberId);

        return new TokenResponse(accessToken, newRefreshToken.getToken());
    }

    public void block(
            final Long memberId,
            final Long targetId
    ) {
        final Member member = findMember(memberId);
        final Member target = findMember(targetId);

        member.validateSelfBlock(target);
        validateDuplicatedBlock(member, target);

        final Block block = Block.builder()
                .member(member)
                .target(target)
                .build();
        blockRepository.save(block);
    }

    public void report(
            final Long memberId,
            final Long targetId,
            final String reason
    ) {
        final Member member = findMember(memberId);
        final Member target = findMember(targetId);
        final MemberReportReason reportReason = MemberReportReason.fromDescription(reason);

        member.validateSelfReport(target);
        validateDuplicatedReport(member, target);

        final MemberReport memberReport = MemberReport.builder()
                .member(member)
                .target(target)
                .reason(reportReason)
                .build();
        memberReportRepository.save(memberReport);
    }

    public ProfileUpdateResponse updateProfile(
            final Long memberId,
            final ProfileUpdateRequest profileUpdateRequest
    ) {
        final Member member = findMember(memberId);

        final String newNickname = profileUpdateRequest.nickname();
        final String newProfileMessage = profileUpdateRequest.profileMessage();

        validateNicknameUpdate(member, newNickname);
        member.updateNicknameAndProfileMessage(newNickname, newProfileMessage);

        return new ProfileUpdateResponse(member);
    }

    public ProfileImageUpdateResponse updateProfileImage(
            final Long memberId,
            final MultipartFile profileImage
    ) {
        final Member member = findMember(memberId);

        validateUpdateProfileImage(member, profileImage);

        final ProfileImageResponse profileImageResponse = s3ImageUploadClient.uploadImage(profileImage);

        final String downloadUrl = profileImageResponse.downloadUrl();
        member.updateProfileImage(downloadUrl);

        return new ProfileImageUpdateResponse(downloadUrl);
    }

    public void deleteMember(
            final Long accessTokenMemberId,
            final RefreshTokenRequest refreshTokenRequest
    ) {
        final TokenInfo tokenInfo = jwtTokenProvider.getInfoByRefreshToken(refreshTokenRequest.refreshToken());
        final Long refreshTokenMemberId = tokenInfo.id();
        validateTokenMemberId(accessTokenMemberId, refreshTokenMemberId);

        final Member member = findMember(accessTokenMemberId);
        final RefreshToken refreshToken = findRefreshToken(refreshTokenRequest.refreshToken());

        memberRepository.delete(member);
        refreshTokenRepository.delete(refreshToken);
    }

    public void deleteBlock(
            final Long memberId,
            final Long targetId
    ) {
        final Member member = findMember(memberId);
        final Member target = findMember(targetId);

        validateBlock(member, target);

        final Block block = blockRepository.findByMemberAndTarget(member, target);

        blockRepository.delete(block);
    }

    private void validateDuplicatedNickname(final String nickname) {
        if (memberRepository.existsByNicknameAndDeletedAtIsNull(nickname)) {
            throw new IllegalArgumentException(String.format("이미 존재하는 닉네임입니다: nickname = %s", nickname));
        }
    }

    private void validateDuplicatedEmail(final SignupRequest signupRequest) {
        if (memberRepository.existsByEmailAndDeletedAtIsNull(signupRequest.email())) {
            throw new IllegalArgumentException(
                    String.format("이미 가입된 이메일입니다: email = %s", maskEmail(signupRequest.email())));
        }
    }

    private void validateEmailWithTokenEmail(
            final SignupRequest signupRequest,
            final String tokenEmail
    ) {
        if (!tokenEmail.equals(signupRequest.email())) {
            throw new IllegalArgumentException(
                    String.format("소셜 로그인을 하지 않은 이메일입니다: email = %s", maskEmail(signupRequest.email())));
        }
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(
                        () -> new NoSuchElementException(String.format("해당 회원을 찾을 수 없습니다: memberId = %s", memberId)));
    }

    private void saveRefreshTokenIfUnique(
            final RefreshToken refreshToken,
            final Long memberId
    ) {
        try {
            validateDuplicatedRefreshToken(refreshToken, memberId);

            refreshTokenRepository.save(refreshToken);
        } catch (final DataIntegrityViolationException e) {
            throw new ConcurrentModificationException(
                    String.format("중복된 리프레시 토큰 발급 요청입니다: memberId = %d", memberId));
        }
    }

    private void validateDuplicatedRefreshToken(
            final RefreshToken refreshToken,
            final Long memberId
    ) {
        if (refreshTokenRepository.existsByToken(refreshToken.getToken())) {
            throw new ConcurrentModificationException(String.format("중복된 리프레시 토큰 발급 요청입니다: memberId = %d", memberId));
        }
    }

    private static void validateTokenMemberId(
            final Long accessTokenMemberId,
            final Long refreshTokenMemberId
    ) {
        if (!refreshTokenMemberId.equals(accessTokenMemberId)) {
            throw new IllegalArgumentException(
                    String.format("리프레시 토큰과 액세스 토큰의 회원 정보가 일치하지 않습니다: accessToken memberId = %d, refreshToken memberId = %d",
                            accessTokenMemberId, refreshTokenMemberId)
            );
        }
    }

    private void validateDuplicatedBlock(
            final Member member,
            final Member target
    ) {
        if (blockRepository.existsByMemberAndTarget(member, target)) {
            throw new IllegalArgumentException(
                    String.format("이미 차단한 회원입니다: memberId = %s -> targetId = %s", member.getId(), target.getId()));
        }
    }

    private void validateDuplicatedReport(
            final Member member,
            final Member target
    ) {
        if (memberReportRepository.existsByMemberAndTarget(member, target)) {
            throw new IllegalArgumentException(
                    String.format("이미 신고한 회원입니다: memberId = %s -> targetId = %s", member.getId(), target.getId()));
        }
    }

    private void validateNicknameUpdate(
            final Member member,
            final String newNickname
    ) {
        if (member.isMyNickname(newNickname)) {
            return;
        }
        validateDuplicatedNickname(newNickname);
    }

    private void validateBlock(
            final Member member,
            final Member target
    ) {
        if (!blockRepository.existsByMemberAndTarget(member, target)) {
            throw new IllegalArgumentException(
                    String.format("차단한 회원이 아닙니다: memberId = %s -> targetId = %s", member.getId(), target.getId()));
        }
    }

    private String maskEmail(final String email) {
        final String visiblePart = email.substring(0, 4);
        final String maskedPart = "*".repeat(email.length() - 4);
        return visiblePart + maskedPart;
    }

    private RefreshToken findRefreshToken(final String oldRefreshToken) {
        return refreshTokenRepository.findByToken(oldRefreshToken)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("해당 리프레시 토큰을 찾을 수 없습니다: refreshToken = %s", oldRefreshToken)));
    }

    private void validateUpdateProfileImage(
            final Member member,
            final MultipartFile profileImage
    ) {
        validateIsProfileImageEmpty(member, profileImage);
        validateProfileImageSize(member, profileImage);
        validateProfileImageType(member, profileImage);

    }

    private void validateIsProfileImageEmpty(
            final Member member,
            final MultipartFile profileImage
    ) {
        if (profileImage.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("파일이 비어있습니다: memberId = %s", member.getId())
            );
        }
    }

    private void validateProfileImageSize(
            final Member member,
            final MultipartFile profileImage
    ) {
        final long imageSize = profileImage.getSize();
        if (imageSize > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                    String.format("파일 크기가 5MB 초과입니다: memberId = %s, imageSize = %d", member.getId(), imageSize)
            );
        }
    }

    private void validateProfileImageType(
            final Member member,
            final MultipartFile profileImage
    ) {
        final String contentType = profileImage.getContentType();
        if (!ImageType.contains(contentType)) {
            throw new IllegalArgumentException(
                    String.format("이미지 파일이 아닙니다: memberId = %s, contentType = %s", member.getId(), contentType)
            );
        }
    }
}
