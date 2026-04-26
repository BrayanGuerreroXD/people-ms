package co.com.pragma.peoplems.api.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenericResponseDataTest {

    @Test
    void ofShouldWrapDataUnderDataKey() {
        String payload = "hello";
        GenericResponseData<String> result = GenericResponseData.of(payload);
        assertThat(result.getData()).isEqualTo("hello");
    }

    @Test
    void ofShouldAcceptNullData() {
        GenericResponseData<String> result = GenericResponseData.of(null);
        assertThat(result.getData()).isNull();
    }
}
