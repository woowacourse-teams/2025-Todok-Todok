package todoktodok.backend.global.exception;

public record ErrorResponse(
        int code,
        String message
) {
}
