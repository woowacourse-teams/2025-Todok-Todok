package todoktodok.backend.member.infrastructure;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import todoktodok.backend.member.application.service.command.GoogleAuthMemberDto;

import java.security.GeneralSecurityException;

public interface AuthClient {

    String resolveVerifiedEmailFrom(final String idTokenRequest);

    GoogleAuthMemberDto resolveVerifiedEmailAndNicknameFrom(final String idTokenRequest);
}
