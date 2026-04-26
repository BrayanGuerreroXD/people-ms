package co.com.pragma.peoplems.model.exception;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionEnumTest {

    @Test
    void enumShouldContainExpectedValues() {
        assertThat(GlobalExceptionEnum.values()).hasSize(2);
        assertThat(GlobalExceptionEnum.values())
                .extracting(GlobalExceptionEnum::name)
                .containsExactly("PERSON_NOT_FOUND", "EMAIL_ALREADY_EXISTS");
    }

    @Test
    void enumShouldExposeStringAccessors() {
        Function<GlobalExceptionEnum, String> msg  = GlobalExceptionEnum::getMessage;
        Function<GlobalExceptionEnum, String> desc = GlobalExceptionEnum::getDescription;
        assertThat(msg).isNotNull();
        assertThat(desc).isNotNull();
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
