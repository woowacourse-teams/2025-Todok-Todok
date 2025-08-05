package todoktodok.backend.member.application.service.query;

import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.member.application.dto.response.ProfileResponse;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public ProfileResponse getProfile(final Long memberId) {
        final Member member = findMember(memberId);

        return new ProfileResponse(member);
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 회원을 찾을 수 없습니다"));
    }
}
