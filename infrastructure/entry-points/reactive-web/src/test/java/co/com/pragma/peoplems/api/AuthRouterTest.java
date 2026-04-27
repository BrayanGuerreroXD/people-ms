package co.com.pragma.peoplems.api;

import co.com.pragma.peoplems.api.handler.AuthHandler;
import co.com.pragma.peoplems.model.auth.Auth;
import co.com.pragma.peoplems.usecase.login.LoginService;
import co.com.pragma.peoplems.usecase.logout.LogoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@ExtendWith(MockitoExtension.class)
class AuthRouterTest {

    @Mock private LoginService loginService;
    @Mock private LogoutService logoutService;

    private WebTestClient webTestClient;
    private AuthHandler authHandler;

    @BeforeEach
    void setup() {
        authHandler = new AuthHandler(loginService, logoutService);
        RouterFunction<ServerResponse> routes = route(POST("/api/auth/login"), authHandler::login)
                .andRoute(POST("/api/auth/logout"), authHandler::logout);
        webTestClient = WebTestClient.bindToRouterFunction(routes).build();
    }

    @Test
    void login_withValidCredentials_returns200WithToken() {
        Auth result = Auth.builder().email("user@test.com").token("jwt-token").build();
        when(loginService.login(any(Auth.class))).thenReturn(Mono.just(result));

        webTestClient.post().uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"email\":\"user@test.com\",\"password\":\"raw-pw\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.data.token").isEqualTo("jwt-token");
    }

    @Test
    void logout_withValidToken_returns204() {
        when(logoutService.logout(anyString())).thenReturn(Mono.empty());

        webTestClient.post().uri("/api/auth/logout")
                .header("Authorization", "Bearer valid-jwt")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void logout_withoutToken_returnsError() {
        webTestClient.post().uri("/api/auth/logout")
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
