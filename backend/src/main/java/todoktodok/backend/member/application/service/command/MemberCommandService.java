package todoktodok.backend.member.application.service.command;

import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.global.jwt.JwtTokenProvider;
import todoktodok.backend.member.application.dto.request.LoginRequest;
import todoktodok.backend.member.application.dto.request.ProfileUpdateRequest;
import todoktodok.backend.member.application.dto.request.SignupRequest;
import todoktodok.backend.member.application.dto.response.ProfileUpdateResponse;
import todoktodok.backend.member.domain.Block;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.MemberReport;
import todoktodok.backend.member.domain.MemberReportReason;
import todoktodok.backend.member.domain.repository.BlockRepository;
import todoktodok.backend.member.domain.repository.MemberReportRepository;
import todoktodok.backend.member.domain.repository.MemberRepository;

@Service
@Transactional
@AllArgsConstructor
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final BlockRepository blockRepository;
    private final MemberReportRepository memberReportRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(final LoginRequest loginRequest) {
        final Optional<Member> member = memberRepository.findByEmail(loginRequest.email());
        if (member.isPresent()) {
            return jwtTokenProvider.createToken(member.get());
        }
        return jwtTokenProvider.createTempToken(loginRequest.email());
    }

    public String signup(
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
        return jwtTokenProvider.createToken(savedMember);
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
        if (memberRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException(String.format("이미 존재하는 닉네임입니다: nickname = %s", nickname));
        }
    }

    private void validateDuplicatedEmail(final SignupRequest signupRequest) {
        if (memberRepository.existsByEmail(signupRequest.email())) {
            throw new IllegalArgumentException(String.format("이미 가입된 이메일입니다: email = %s", maskEmail(signupRequest.email())));
        }
    }

    private void validateEmailWithTokenEmail(
            final SignupRequest signupRequest,
            final String tokenEmail
    ) {
        if (!tokenEmail.equals(signupRequest.email())) {
            throw new IllegalArgumentException(String.format("소셜 로그인을 하지 않은 이메일입니다: email = %s", maskEmail(signupRequest.email())));
        }
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException(String.format("해당 회원을 찾을 수 없습니다: memberId = %s", memberId)));
    }

    private void validateDuplicatedBlock(
            final Member member,
            final Member target
    ) {
        if (blockRepository.existsByMemberAndTarget(member, target)) {
            throw new IllegalArgumentException(String.format("이미 차단한 회원입니다: memberId = %s -> targetId = %s", member.getId(), target.getId()));
        }
    }

    private void validateDuplicatedReport(
            final Member member,
            final Member target
    ) {
        if (memberReportRepository.existsByMemberAndTarget(member, target)) {
            throw new IllegalArgumentException(String.format("이미 신고한 회원입니다: memberId = %s -> targetId = %s", member.getId(), target.getId()));
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
            throw new IllegalArgumentException(String.format("차단한 회원이 아닙니다: memberId = %s -> targetId = %s", member.getId(), target.getId()));
        }
    }

    private String maskEmail(final String email) {
        final String visiblePart = email.substring(0, 4);
        final String maskedPart = "*".repeat(email.length() - 4);
        return visiblePart + maskedPart;
    }
}
