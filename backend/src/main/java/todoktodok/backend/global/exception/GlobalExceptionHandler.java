package todoktodok.backend.global.exception;

import io.jsonwebtoken.JwtException;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String PREFIX = "[ERROR] ";

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(final JwtException e) {
        final HttpStatus status = HttpStatus.UNAUTHORIZED;

        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), PREFIX + e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(final IllegalArgumentException e) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        log.warn(PREFIX + e.getMessage());

        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), PREFIX + getSafeErrorMessage(e)));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(final NoSuchElementException e) {
        final HttpStatus status = HttpStatus.NOT_FOUND;
        log.warn(PREFIX + e.getMessage());

        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), PREFIX + getSafeErrorMessage(e)));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(final IllegalStateException e) {
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.warn(PREFIX + e.getMessage());

        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), PREFIX + getSafeErrorMessage(e)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException e
    ) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        log.warn(PREFIX + e.getBindingResult().getFieldErrors().getFirst().getDefaultMessage());
        e.getBindingResult().getFieldErrors()
                .forEach(fieldError -> {
                    Object rejectedValue = fieldError.getRejectedValue();
                    String maskedValue = maskEmailValue(rejectedValue, fieldError.getField());
                    log.warn("{}: {}", fieldError.getField(), maskedValue);
                });

        return ResponseEntity.status(status)
                .body(new ErrorResponse(
                        status.value(),
                        PREFIX + e.getBindingResult().getFieldErrors().getFirst().getDefaultMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            final MethodArgumentTypeMismatchException e
    ) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        log.warn(PREFIX + String.format("유효하지 않은 %s의 값입니다", e.getRequiredType().getSimpleName()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        status.value(),
                        PREFIX + String.format("유효하지 않은 %s의 값입니다", e.getRequiredType().getSimpleName())
                ));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            final MissingServletRequestParameterException e
    ) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        log.warn(PREFIX + String.format("파라미터 %s가 존재하지 않습니다", e.getParameterName()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        status.value(),
                        PREFIX + String.format("파라미터 %s가 존재하지 않습니다", e.getParameterName())
                ));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(final RuntimeException e) {
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("Unexpected error occurred", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        status.value(), PREFIX + "예상하지 못한 예외가 발생하였습니다. 상세 정보: " + e.getMessage()
                ));
    }

    private String maskEmailValue(
            final Object value,
            final String field
    ) {
        if (value == null) {
            return null;
        }

        final String str = value.toString();
        if (!field.equals("email")) {
            return str;
        }

        if (str.length() <= 4) {
            return str;
        }

        final String visiblePart = str.substring(0, 4);
        final String maskedPart = "*".repeat(str.length() - 4);
        return visiblePart + maskedPart;
    }

    private String getSafeErrorMessage(final RuntimeException e) {
        return e.getMessage().split(":")[0];
    }
}
