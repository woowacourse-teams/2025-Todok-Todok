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

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(final IllegalStateException e) {
        log.warn(PREFIX + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(PREFIX + getSafeErrorMessage(e));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        final List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        final String errorMessage = fieldErrors.getFirst().getDefaultMessage();

        fieldErrors.forEach(fieldError -> {
            final Object rejectedValue = fieldError.getRejectedValue();
            final String maskedValue = maskEmailValue(rejectedValue, fieldError.getField());
            log.warn(String.format("%s: %s = %s",
                    PREFIX + errorMessage,
                    fieldError.getField(),
                    maskedValue));
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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(final RuntimeException e) {
        log.error("Unexpected error occurred", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(PREFIX + "예상하지 못한 예외가 발생하였습니다. 상세 정보: " + e.getMessage());
    }

    @ExceptionHandler(AladinApiException.class)
    public ResponseEntity<String> handleAladinApiException(final AladinApiException e) {
        log.error(PREFIX + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(PREFIX + getSafeErrorMessage(e));
    }

    private String maskEmailValue(
            final Object value,
            final String field
    ) {
        if (value == null) {
            return null;
        }

        final String str = value.toString();

        if (field.equals("discussionTitle") || field.equals("discussionOpinion")) {
            return str.length() + "자";
        }

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
