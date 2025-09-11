package todoktodok.backend.member.infrastructure.exception;

public class AwsApiException extends RuntimeException {

    public AwsApiException(String message) {
        super(message);
    }
}
