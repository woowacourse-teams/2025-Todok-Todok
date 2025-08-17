package todoktodok.backend.global.exception;

import io.jsonwebtoken.JwtException;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import todoktodok.backend.book.infrastructure.aladin.exception.AladinApiException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String PREFIX = "[ERROR] ";

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> handleJwtException(final JwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(PREFIX + e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequestException(final IllegalArgumentException e) {
        log.warn(PREFIX + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(PREFIX + getSafeErrorMessage(e));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFoundException(final NoSuchElementException e) {
        log.warn(PREFIX + e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(PREFIX + getSafeErrorMessage(e));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        final List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        final String errorMessage = fieldErrors.getFirst().getDefaultMessage();

        fieldErrors.forEach(fieldError -> {
            final Object rejectedValue = fieldError.getRejectedValue();
            final String safeLogValue = toSafeLogValue(rejectedValue, fieldError.getField());
            log.warn(String.format("%s: %s = %s",
                    PREFIX + fieldError.getDefaultMessage(),
                    fieldError.getField(),
                    safeLogValue));
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(PREFIX + errorMessage);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(
            final MethodArgumentTypeMismatchException e
    ) {
        log.warn(PREFIX + String.format("유효하지 않은 %s의 값입니다", e.getRequiredType().getSimpleName()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(PREFIX + String.format("유효하지 않은 %s의 값입니다", e.getRequiredType().getSimpleName()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingServletRequestParameterException(
            final MissingServletRequestParameterException e
    ) {
        log.warn(PREFIX + String.format("파라미터 %s가 존재하지 않습니다", e.getParameterName()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(PREFIX + String.format("파라미터 %s가 존재하지 않습니다", e.getParameterName()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(final IllegalStateException e) {
        log.error(PREFIX + e.getMessage());
        return ResponseEntity.internalServerError().body(PREFIX + getSafeErrorMessage(e));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(final RuntimeException e) {
        log.error(String.format("Unexpected error occurred: %s", e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(PREFIX + "서버 내부 오류가 발생했습니다");
    }

    @ExceptionHandler(AladinApiException.class)
    public ResponseEntity<String> handleAladinApiException(final AladinApiException e) {
        log.error(PREFIX + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(PREFIX + getSafeErrorMessage(e));
    }

    private String toSafeLogValue(
            final Object value,
            final String field
    ) {
        if (value == null) {
            return null;
        }

        final String str = value.toString();

        if ("discussionTitle".equals(field)
                || "discussionOpinion".equals(field)
                || "content".equals(field)
        ) {
            return str.length() + "자";
        }

        if ("email".equals(field)) {
            if (str.length() <= 4) {
                return str;
            }
            final String visiblePart = str.substring(0, 4);
            final String maskedPart = "*".repeat(str.length() - 4);
            return visiblePart + maskedPart;
        }

        return str;
    }

    private String getSafeErrorMessage(final RuntimeException e) {
        return e.getMessage().split(":")[0];
    }
}
