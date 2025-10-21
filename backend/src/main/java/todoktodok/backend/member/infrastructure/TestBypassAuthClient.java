package todoktodok.backend.member.infrastructure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import todoktodok.backend.member.application.service.command.GoogleAuthMemberDto;

@Component
@ConditionalOnProperty(name = "auth.mode", havingValue = "test", matchIfMissing = true)
public class TestBypassAuthClient implements AuthClient {

    @Override
    public String resolveVerifiedEmailFrom(final String idTokenRequest) {
        return idTokenRequest;
    }

    @Override
    public GoogleAuthMemberDto resolveVerifiedEmailAndNicknameFrom(final String idTokenRequest) {
        return new GoogleAuthMemberDto(idTokenRequest, "");
    }
}
