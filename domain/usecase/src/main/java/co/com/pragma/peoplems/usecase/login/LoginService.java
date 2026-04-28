package co.com.pragma.peoplems.usecase.login;

import co.com.pragma.peoplems.model.auth.Auth;
import reactor.core.publisher.Mono;

public interface LoginService {
    Mono<Auth> login(Auth auth);
}
