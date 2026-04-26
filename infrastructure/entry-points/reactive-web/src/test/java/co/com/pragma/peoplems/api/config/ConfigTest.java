package co.com.pragma.peoplems.api.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigTest {

    @Test
    void corsConfigShouldExist() {
        CorsConfig corsConfig = new CorsConfig();
        assertThat(corsConfig).isNotNull();
    }

}