package co.com.pragma.peoplems.model.security;

import reactor.core.publisher.Mono;

public interface UserContext {
    Mono<LoggedUser> currentUser();
}
