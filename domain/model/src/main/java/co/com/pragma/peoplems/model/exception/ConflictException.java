package co.com.pragma.peoplems.model.exception;

public class ConflictException extends RuntimeException {
    private final GlobalExceptionEnum error;

    public ConflictException(GlobalExceptionEnum error) {
        super(error.getMessage());
        this.error = error;
    }

    public GlobalExceptionEnum getError() {
        return error;
    }
}
