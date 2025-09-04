package todoktodok.backend.member.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import todoktodok.backend.global.common.TimeStamp;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@SQLRestriction("deleted_at is NULL")
@SQLDelete(sql = "UPDATE refresh_token SET deleted_at = NOW() WHERE id = ?")
public class RefreshToken extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Builder
    public static RefreshToken create(
            final String token
    ) {
        validateToken(token);
        return new RefreshToken(null, token);
    }

    private static void validateToken(final String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("토큰은 1자 이상이어야 합니다");
        }
    }
}
