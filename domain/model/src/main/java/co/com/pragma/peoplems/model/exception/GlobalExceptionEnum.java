package co.com.pragma.peoplems.model.exception;

public enum GlobalExceptionEnum {
    PERSON_NOT_FOUND("Person not found", "No person found with the provided identifier"),
    EMAIL_ALREADY_EXISTS("Email already exists", "A person with the provided email already exists");

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
