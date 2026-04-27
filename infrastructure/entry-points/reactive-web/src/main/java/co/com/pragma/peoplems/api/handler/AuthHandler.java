package co.com.pragma.peoplems.api.handler;

import co.com.pragma.peoplems.api.dto.GenericResponseData;
import co.com.pragma.peoplems.api.dto.LoginRequest;
import co.com.pragma.peoplems.api.dto.TokenResponse;
import co.com.pragma.peoplems.model.auth.Auth;
import co.com.pragma.peoplems.model.exception.GlobalExceptionEnum;
import co.com.pragma.peoplems.model.exception.UnauthorizedException;
import co.com.pragma.peoplems.usecase.login.LoginService;
import co.com.pragma.peoplems.usecase.logout.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final LoginService loginService;
    private final LogoutService logoutService;

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .map(req -> Auth.builder()
                        .email(req.getEmail())
                        .password(req.getPassword())
                        .build())
                .flatMap(loginService::login)
                .map(auth -> TokenResponse.builder().token(auth.getToken()).build())
                .map(GenericResponseData::of)
                .flatMap(body -> ServerResponse.ok().bodyValue(body));
    }

    public Mono<ServerResponse> logout(ServerRequest request) {
        String authHeader = request.headers().firstHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.error(new UnauthorizedException(GlobalExceptionEnum.INVALID_TOKEN));
        }
        String token = authHeader.substring(7);
        return logoutService.logout(token)
                .then(ServerResponse.status(HttpStatus.NO_CONTENT).build());
    }
}
