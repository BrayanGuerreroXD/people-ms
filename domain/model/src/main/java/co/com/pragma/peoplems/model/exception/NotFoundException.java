package co.com.pragma.peoplems.model.exception;

public class NotFoundException extends RuntimeException {
    private final GlobalExceptionEnum error;

    public NotFoundException(GlobalExceptionEnum error) {
        super(error.getMessage());
        this.error = error;
    }

    public GlobalExceptionEnum getError() {
        return error;
    }
}
