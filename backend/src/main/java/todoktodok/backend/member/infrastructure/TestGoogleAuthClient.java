package todoktodok.backend.member.infrastructure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import todoktodok.backend.member.application.service.command.GoogleAuthMemberDto;

@Component
@ConditionalOnProperty(name = "auth.mode", havingValue = "test-google", matchIfMissing = true)
public class TestGoogleAuthClient implements AuthClient{

    @Override
    public String resolveVerifiedEmailFrom(final String idTokenRequest) {
        // idTokenRequest를 email로 그대로 사용
        // 클라이언트가 test1~test15000@gmail.com 형식으로 전송
        return idTokenRequest;
    }

    @Override
    public GoogleAuthMemberDto resolveVerifiedEmailAndNicknameFrom(final String idTokenRequest) {
        // 로그인 시와 동일한 email 사용 (temp token에 저장된 값)
        return new GoogleAuthMemberDto(idTokenRequest, "");
    }
}
