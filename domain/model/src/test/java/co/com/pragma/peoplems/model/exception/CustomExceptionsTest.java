package co.com.pragma.peoplems.model.exception;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CustomExceptionsTest {

    static Stream<Class<? extends RuntimeException>> exceptionTypes() {
        return Stream.of(
            BadRequestException.class,
            NotFoundException.class,
            InternalServerErrorException.class,
            ForbiddenException.class,
            UnauthorizedException.class,
            ConflictException.class
        );
    }

    @ParameterizedTest
    @MethodSource("exceptionTypes")
    void eachExceptionShouldExtendRuntimeException(Class<?> type) {
        assertThat(type).hasSuperclass(RuntimeException.class);
    }

    @ParameterizedTest
    @MethodSource("exceptionTypes")
    void eachExceptionShouldAcceptGlobalExceptionEnumConstructor(Class<?> type)
            throws NoSuchMethodException {
        var constructor = type.getDeclaredConstructor(GlobalExceptionEnum.class);
        assertThat(constructor).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("exceptionTypes")
    void eachExceptionShouldExposeGetErrorMethod(Class<?> type)
            throws NoSuchMethodException {
        assertThat(type.getMethod("getError").getReturnType())
            .isEqualTo(GlobalExceptionEnum.class);
    }
}
