package co.com.pragma.peoplems.model.exception;

public class UnauthorizedException extends RuntimeException {
    private final GlobalExceptionEnum error;

    public UnauthorizedException(GlobalExceptionEnum error) {
        super(error.getMessage());
        this.error = error;
    }

    public GlobalExceptionEnum getError() {
        return error;
    }
}
