package todoktodok.backend.global.exception;

import io.jsonwebtoken.JwtException;
import java.time.DateTimeException;
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
    public ResponseEntity<String> handleJwtException(final JwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(PREFIX + e.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class,
            DateTimeException.class})
    public ResponseEntity<String> handleBadRequestException(final RuntimeException e) {
        return ResponseEntity.badRequest().body(PREFIX + e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(final IllegalStateException e) {
        return ResponseEntity.internalServerError().body(PREFIX + e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
                .body(PREFIX + e.getBindingResult().getFieldErrors().getFirst().getDefaultMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        return ResponseEntity.badRequest()
                .body(PREFIX + String.format("유효하지 않은 %s의 값입니다", e.getRequiredType().getName()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        return ResponseEntity.badRequest()
                .body(PREFIX + String.format("파라미터 %s가 존재하지 않습니다", e.getParameterName()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(final RuntimeException e) {
        log.error("Unexpected error occurred", e);
        return ResponseEntity.internalServerError().body(PREFIX + "예상하지 못한 예외가 발생하였습니다. 상세 정보: " + e.getMessage());
    }
}
