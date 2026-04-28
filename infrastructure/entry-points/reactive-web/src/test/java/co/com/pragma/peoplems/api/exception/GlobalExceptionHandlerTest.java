package co.com.pragma.peoplems.api.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.HttpHandlerConnector;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

class GlobalExceptionHandlerTest {

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler(new ObjectMapper());

        var httpHandler = WebHttpHandlerBuilder
            .webHandler(exchange -> Mono.error(new RuntimeException("unexpected")))
            .exceptionHandler(handler)
            .build();

        webTestClient = WebTestClient.bindToServer(new HttpHandlerConnector(httpHandler)).build();
    }

    @Test
    void shouldReturn500AndUnknownErrorCodeForUnhandledException() {
        webTestClient.get().uri("/any")
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
            .expectHeader().contentType("application/json")
            .expectBody()
            .jsonPath("$.data.errorCode").isEqualTo("UNKNOWN")
            .jsonPath("$.data.message").isEqualTo("Internal Server Error")
            .jsonPath("$.data.description").isEqualTo("An unexpected error occurred");
    }
}
