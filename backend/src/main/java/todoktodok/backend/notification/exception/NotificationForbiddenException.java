package todoktodok.backend.notification.exception;

public class NotificationForbiddenException extends RuntimeException {
    public NotificationForbiddenException(String message) {
        super(message);
    }
}
