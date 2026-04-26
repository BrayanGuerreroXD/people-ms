package co.com.pragma.peoplems.model.exception;

public enum GlobalExceptionEnum {
    ;

    private final String message;
    private final String description;

    GlobalExceptionEnum(String message, String description) {
        this.message = message;
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
