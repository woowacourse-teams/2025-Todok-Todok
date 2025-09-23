package todoktodok.backend.member.infrastructure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import todoktodok.backend.member.application.service.command.GoogleAuthMemberDto;

@Component
@ConditionalOnProperty(name = "auth.mode", havingValue = "bypass", matchIfMissing = true)
public class LocalBypassAuthClient implements AuthClient{

    @Override
    public String resolveVerifiedEmailFrom(String idTokenRequest) {
        return idTokenRequest;
    }

    @Override
    public GoogleAuthMemberDto resolveVerifiedEmailAndNicknameFrom(String idTokenRequest) {
        return new GoogleAuthMemberDto(idTokenRequest, "");
    }
}
