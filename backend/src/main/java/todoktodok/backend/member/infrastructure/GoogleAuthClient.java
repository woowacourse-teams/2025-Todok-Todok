package todoktodok.backend.member.infrastructure;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import todoktodok.backend.member.application.service.command.GoogleAuthMemberDto;

import java.security.GeneralSecurityException;

@Component
@AllArgsConstructor
@ConditionalOnProperty(name = "auth.mode", havingValue = "google", matchIfMissing = true)
public class GoogleAuthClient implements AuthClient{

    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    public String resolveVerifiedEmailFrom(final String idTokenRequest) {
        try {
            final GoogleIdToken idToken = googleIdTokenVerifier.verify(idTokenRequest);

            validateResolvedIdToken(idToken, idTokenRequest);

            final GoogleIdToken.Payload payload = idToken.getPayload();
            return payload.getEmail();
        } catch (final GeneralSecurityException e) {
            throw new IllegalArgumentException(String.format("유효하지 않은 토큰입니다 : idToken = %s", maskToken(idTokenRequest)));
        } catch (final Exception e) {
            throw new IllegalStateException(String.format("토큰 검증 중 오류가 발생했습니다 : idToken = %s", maskToken(idTokenRequest)));
        }
    }

    public GoogleAuthMemberDto resolveVerifiedEmailAndNicknameFrom(final String idTokenRequest) {
        try {
            final GoogleIdToken idToken = googleIdTokenVerifier.verify(idTokenRequest);

            validateResolvedIdToken(idToken, idTokenRequest);

            final GoogleIdToken.Payload payload = idToken.getPayload();
            return new GoogleAuthMemberDto(
                    payload.getEmail(),
                    (String) payload.get("picture")
            );
        } catch (final GeneralSecurityException e) {
            throw new IllegalArgumentException(String.format("유효하지 않은 토큰입니다 : idToken = %s", maskToken(idTokenRequest)));
        } catch (final Exception e) {
            throw new IllegalStateException(String.format("토큰 검증 중 오류가 발생했습니다 : idToken = %s", maskToken(idTokenRequest)));
        }
    }

    private void validateResolvedIdToken(
            final GoogleIdToken idToken,
            final String idTokenRequest
    ) {
        if (idToken == null) {
            throw new IllegalArgumentException(String.format("유효하지 않은 토큰입니다 : idToken = %s", maskToken(idTokenRequest)));
        }
    }

    private String maskToken(final String token) {
        if (token == null || token.length() < 8) {
            return "****";
        }
        return token.substring(0, 4) + "****" + token.substring(token.length() - 4);
    }
}
