package todoktodok.backend.member.application.service.command;

import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.global.jwt.JwtTokenProvider;
import todoktodok.backend.member.application.dto.request.LoginRequest;
import todoktodok.backend.member.application.dto.request.SignupRequest;
import todoktodok.backend.member.domain.Block;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.MemberReport;
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
        validateDuplicatedNickname(signupRequest);
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

        validateSelfBlock(member, target);
        validateDuplicatedBlock(member, target);

        final Block block = Block.builder()
                .member(member)
                .target(target)
                .build();
        blockRepository.save(block);
    }

    public void report(
            final Long memberId,
            final Long targetId
    ) {
        final Member member = findMember(memberId);
        final Member target = findMember(targetId);

        validateSelfReport(member, target);
        validateDuplicatedReport(member, target);

        final MemberReport memberReport = MemberReport.builder()
                .member(member)
                .target(target)
                .build();
        memberReportRepository.save(memberReport);
    }

    private void validateDuplicatedNickname(final SignupRequest signupRequest) {
        if (memberRepository.existsByNickname(signupRequest.nickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다");
        }
    }

    private void validateDuplicatedEmail(final SignupRequest signupRequest) {
        if (memberRepository.existsByEmail(signupRequest.email())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다");
        }
    }

    private void validateEmailWithTokenEmail(
            final SignupRequest signupRequest,
            final String tokenEmail
    ) {
        if (!tokenEmail.equals(signupRequest.email())) {
            throw new IllegalArgumentException("소셜 로그인을 하지 않은 이메일입니다");
        }
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 회원을 찾을 수 없습니다"));
    }

    private static void validateSelfBlock(
            final Member member,
            final Member target
    ) {
        if (member.equals(target)) {
            throw new IllegalArgumentException("자기 자신을 차단할 수 없습니다");
        }
    }

    private void validateDuplicatedBlock(
            final Member member,
            final Member target
    ) {
        if (blockRepository.existsByMemberAndTarget(member, target)) {
            throw new IllegalArgumentException("이미 차단한 회원입니다");
        }
    }

    private static void validateSelfReport(
            final Member member,
            final Member target
    ) {
        if (member.equals(target)) {
            throw new IllegalArgumentException("자기 자신을 신고할 수 없습니다");
        }
    }

    private void validateDuplicatedReport(
            final Member member,
            final Member target
    ) {
        if (memberReportRepository.existsByMemberAndTarget(member, target)) {
            throw new IllegalArgumentException("이미 신고한 회원입니다");
        }
    }
}
