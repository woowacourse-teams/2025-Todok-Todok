package todoktodok.backend.global.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.Role;

@Component
public class JwtTokenProvider {

    private static final SecretKey SECRET_KEY = SIG.HS256.key().build();
    private static final long validityInMilliseconds = 3600000;
    private static final String PREFIX = "token=";

    public String createToken(Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return PREFIX + Jwts.builder()
                .claim("id", member.getId())
                .claim("role", member.getRole())
                .expiration(validity)
                .signWith(SECRET_KEY)
                .compact();
    }

    public String createTempToken() {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return PREFIX + Jwts.builder()
                .claim("role", Role.TEMPUSER)
                .expiration(validity)
                .signWith(SECRET_KEY)
                .compact();
    }
}
