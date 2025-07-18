package todoktodok.backend.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.global.auth.Role;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String JWT_EXCEPTION_MESSAGE = "잘못된 로그인 시도입니다. 다시 시도해 주세요.";
    private static final SecretKey SECRET_KEY = SIG.HS256.key().build();
    private static final long validityInMilliseconds = 3600000;
    private static final long validityTempUserInMilliseconds = 300000;

    public String createToken(final Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .claim("id", member.getId())
                .claim("role", Role.USER)
                .expiration(validity)
                .signWith(SECRET_KEY)
                .compact();
    }

    public String createTempToken() {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityTempUserInMilliseconds);

        return Jwts.builder()
                .claim("role", Role.TEMPUSER)
                .expiration(validity)
                .signWith(SECRET_KEY)
                .compact();
    }

    public TokenInfo getInfo(final String token) {
        String extractedToken = token.replace("Bearer ","");
        Claims claims = validateToken(extractedToken);

        return new TokenInfo(
                claims.get("id", Long.class),
                Role.valueOf(claims.get("role", String.class))
        );
    }

    private Claims validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            log.warn("JWT 토큰이 존재하지 않습니다.");
            throw new JwtException(JWT_EXCEPTION_MESSAGE);
        }

        try {
            return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            throw new JwtException(JWT_EXCEPTION_MESSAGE);
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token, 만료된 JWT token 입니다.");
            throw new JwtException(JWT_EXCEPTION_MESSAGE);
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
            throw new JwtException(JWT_EXCEPTION_MESSAGE);
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
            throw new JwtException(JWT_EXCEPTION_MESSAGE);
        }
    }
}
