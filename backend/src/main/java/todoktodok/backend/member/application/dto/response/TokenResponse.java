package todoktodok.backend.member.application.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
