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

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.member.domain.Member;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String JWT_EXCEPTION_MESSAGE = "잘못된 로그인 시도입니다. 다시 시도해 주세요";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static SecretKey SECRET_KEY;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token.expire-mills}")
    private long validityInMilliseconds;

    @Value("${jwt.temp-token.expire-mills}")
    private long validityTempUserInMilliseconds;

    @PostConstruct
    public void init() {
        SECRET_KEY = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(final Member member) {
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + validityInMilliseconds);

        return TOKEN_PREFIX + Jwts.builder()
                .subject(member.getId().toString())
                .claim("role", Role.USER)
                .expiration(validity)
                .signWith(SECRET_KEY)
                .compact();
    }

    public String createTempToken(final String email) {
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + validityTempUserInMilliseconds);

        return TOKEN_PREFIX + Jwts.builder()
                .claim("email", email)
                .claim("role", Role.TEMP_USER)
                .expiration(validity)
                .signWith(SECRET_KEY)
                .compact();
    }

    public TokenInfo getInfo(final String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            log.warn("유효한 JWT 토큰이 아닙니다");
            throw new JwtException(JWT_EXCEPTION_MESSAGE);
        }

        final String extractedToken = token.replace("Bearer ","");
        final Claims claims = validateToken(extractedToken);

        if (claims.getSubject() == null) {
            return new TokenInfo(
                    null,
                    claims.get("email", String.class),
                    Role.valueOf(claims.get("role", String.class))
            );
        }
        return new TokenInfo(
                Long.valueOf(claims.getSubject()),
                null,
                Role.valueOf(claims.get("role", String.class))
        );
    }

    private Claims validateToken(final String token) {
        if (token == null || token.trim().isEmpty()) {
            log.warn("JWT 토큰이 존재하지 않습니다");
            throw new JwtException(JWT_EXCEPTION_MESSAGE);
        }

        try {
            return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
        } catch (final SecurityException | MalformedJwtException e) {
            log.warn("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다");
            throw new JwtException(JWT_EXCEPTION_MESSAGE);
        } catch (final ExpiredJwtException e) {
            log.warn("Expired JWT token, 만료된 JWT token 입니다");
            throw new JwtException(JWT_EXCEPTION_MESSAGE);
        } catch (final UnsupportedJwtException e) {
            log.warn("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다");
            throw new JwtException(JWT_EXCEPTION_MESSAGE);
        } catch (final IllegalArgumentException e) {
            log.warn("JWT claims is empty, 잘못된 JWT 토큰 입니다");
            throw new JwtException(JWT_EXCEPTION_MESSAGE);
        }
    }
}
