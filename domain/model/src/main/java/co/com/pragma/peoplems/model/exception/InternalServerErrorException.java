package co.com.pragma.peoplems.model.exception;

public class InternalServerErrorException extends RuntimeException {
    private final GlobalExceptionEnum error;

    public InternalServerErrorException(GlobalExceptionEnum error) {
        super(error.getMessage());
        this.error = error;
    }

    public GlobalExceptionEnum getError() {
        return error;
    }
}
