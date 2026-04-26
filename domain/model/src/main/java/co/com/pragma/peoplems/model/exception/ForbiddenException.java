package co.com.pragma.peoplems.model.exception;

public class ForbiddenException extends RuntimeException {
    private final GlobalExceptionEnum error;

    public ForbiddenException(GlobalExceptionEnum error) {
        super(error.getMessage());
        this.error = error;
    }

    public GlobalExceptionEnum getError() {
        return error;
    }
}
