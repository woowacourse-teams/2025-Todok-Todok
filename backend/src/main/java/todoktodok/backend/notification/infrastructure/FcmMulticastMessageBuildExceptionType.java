package todoktodok.backend.notification.infrastructure;

import java.util.Arrays;
import java.util.Optional;

public enum FcmMulticastMessageBuildExceptionType {
    NO_TOKEN("at least one token must be specified", "해당 회원에게 전송할 fcmToken이 없습니다"),
    MAX_TOKEN("no more than 500 tokens can be specified", "fcmToken은 회원당 500개 이하만 가능합니다"),
    EMPTY_TOKEN("none of the tokens can be null or empty", "fcmToken은 null이거나 빈 값일 수 없습니다")
    ;

    private final String exception;
    private final String message;

    FcmMulticastMessageBuildExceptionType(final String exception, final String message) {
        this.exception = exception;
        this.message = message;
    }

    public static String toMessage(final RuntimeException exception) {
        if (exception == null) {
            return "FcmMultiMessage 빌드 중 오류가 발생했습니다 (예외타입 없음)";
        }

        final String exceptionMessage = Optional.ofNullable(exception.getMessage())
                .orElse("FcmMultiMessage 빌드 중 오류가 발생했습니다 (예외메세지 없음)");

        return Arrays.stream(values())
                .filter(ex -> ex.exception.equals(exceptionMessage))
                .findFirst()
                .map(ex -> ex.message)
                .orElse(exceptionMessage);
    }
}
