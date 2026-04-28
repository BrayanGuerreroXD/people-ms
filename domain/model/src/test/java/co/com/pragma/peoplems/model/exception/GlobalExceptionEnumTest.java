package co.com.pragma.peoplems.model.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionEnumTest {

    @Test
    void enumShouldContainExpectedValues() {
        assertThat(GlobalExceptionEnum.values()).hasSize(5);
        assertThat(GlobalExceptionEnum.values())
                .extracting(GlobalExceptionEnum::name)
                .containsExactly("PERSON_NOT_FOUND", "EMAIL_ALREADY_EXISTS", "INVALID_CREDENTIALS", "INVALID_TOKEN", "FORBIDDEN_ACCESS");
    }

    @Test
    void enumShouldExposeStringAccessors() {
        for (GlobalExceptionEnum value : GlobalExceptionEnum.values()) {
            assertThat(value.getMessage()).isNotBlank();
            assertThat(value.getDescription()).isNotBlank();
        }
    }

    @Test
    void personNotFoundShouldHaveCorrectMessages() {
        GlobalExceptionEnum value = GlobalExceptionEnum.PERSON_NOT_FOUND;
        assertThat(value.getMessage()).isEqualTo("Person not found");
        assertThat(value.getDescription()).isEqualTo("No person found with the provided identifier");
    }

    @Test
    void emailAlreadyExistsShouldHaveCorrectMessages() {
        GlobalExceptionEnum value = GlobalExceptionEnum.EMAIL_ALREADY_EXISTS;
        assertThat(value.getMessage()).isEqualTo("Email already exists");
        assertThat(value.getDescription()).isEqualTo("A person with the provided email already exists");
    }
}
