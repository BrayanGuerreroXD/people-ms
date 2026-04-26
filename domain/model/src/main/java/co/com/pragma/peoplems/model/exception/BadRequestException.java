package co.com.pragma.peoplems.model.exception;

public class BadRequestException extends RuntimeException {
    private final GlobalExceptionEnum error;

    public BadRequestException(GlobalExceptionEnum error) {
        super(error.getMessage());
        this.error = error;
    }

    public GlobalExceptionEnum getError() {
        return error;
    }
}
