package todoktodok.backend;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import todoktodok.backend.member.application.service.command.GoogleAuthMemberDto;
import todoktodok.backend.member.infrastructure.AuthClient;

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
