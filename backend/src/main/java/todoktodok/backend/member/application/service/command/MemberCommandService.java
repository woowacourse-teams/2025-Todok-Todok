package todoktodok.backend.member.application.service.command;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.global.jwt.JwtTokenProvider;
import todoktodok.backend.member.application.dto.request.LoginRequest;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;

@Service
@Transactional
@AllArgsConstructor
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(
            final LoginRequest loginRequest
    ) {
        Optional<Member> member = memberRepository.findByEmail(loginRequest.email());
        if (member.isPresent()) {
            return jwtTokenProvider.createToken(member.get());
        }
        return jwtTokenProvider.createTempToken();
    }
}
