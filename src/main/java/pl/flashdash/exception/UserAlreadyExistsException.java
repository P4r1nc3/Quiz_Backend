package pl.flashdash.exception;

public class UserAlreadyExistsException extends Throwable {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
