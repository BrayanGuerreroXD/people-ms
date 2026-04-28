package co.com.pragma.peoplems.usecase.logout;

import reactor.core.publisher.Mono;

public interface LogoutService {
    Mono<Void> logout(String token);
}
