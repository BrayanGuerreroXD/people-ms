package co.com.pragma.peoplems.model.exception;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionEnumTest {

    @Test
    void enumShouldStartEmpty() {
        assertThat(GlobalExceptionEnum.values()).isEmpty();
    }

    @Test
    void enumShouldExposeStringAccessors() {
        Function<GlobalExceptionEnum, String> msg  = GlobalExceptionEnum::getMessage;
        Function<GlobalExceptionEnum, String> desc = GlobalExceptionEnum::getDescription;
        assertThat(msg).isNotNull();
        assertThat(desc).isNotNull();
    }
}
