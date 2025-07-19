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
import todoktodok.backend.member.domain.repository.BlockRepository;
import todoktodok.backend.member.domain.repository.MemberRepository;

@Service
@Transactional
@AllArgsConstructor
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final BlockRepository blockRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(final LoginRequest loginRequest) {
        final Optional<Member> member = memberRepository.findByEmail(loginRequest.email());
        if (member.isPresent()) {
            return jwtTokenProvider.createToken(member.get());
        }
        return jwtTokenProvider.createTempToken();
    }

    public String signup(final SignupRequest signupRequest) {
        validateDuplicatedNickname(signupRequest);

        final Member member = Member.builder()
                .nickname(signupRequest.nickname())
                .email(signupRequest.email())
                .profileImage(signupRequest.profileImage())
                .build();

        final Member savedMember = memberRepository.save(member);
        return jwtTokenProvider.createToken(savedMember);
    }

    private void validateDuplicatedNickname(final SignupRequest signupRequest) {
        if (memberRepository.existsByNickname(signupRequest.nickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다");
        }
    }

    public void block(Long memberId, Long targetId) {
        if (memberId.equals(targetId)) {
            throw new IllegalArgumentException("자기 자신을 차단할 수 없습니다");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 회원을 찾을 수 없습니다"));
        Member target = memberRepository.findById(targetId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 회원을 찾을 수 없습니다"));

        Block block = Block.builder()
                .member(member)
                .target(target)
                .build();
        blockRepository.save(block);
    }
}
