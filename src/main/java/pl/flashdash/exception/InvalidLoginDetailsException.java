package pl.flashdash.exception;

public class InvalidLoginDetailsException extends Throwable {
    public InvalidLoginDetailsException(String message) {
        super(message);
    }
}
