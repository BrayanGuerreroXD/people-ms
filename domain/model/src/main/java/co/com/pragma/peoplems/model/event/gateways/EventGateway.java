package co.com.pragma.peoplems.model.event.gateways;

import co.com.pragma.peoplems.model.event.AuthLoginEvent;
import co.com.pragma.peoplems.model.event.AuthLogoutEvent;
import reactor.core.publisher.Mono;

public interface EventGateway {
    Mono<Void> publishAuthLoginAdmin(AuthLoginEvent event);
    Mono<Void> publishGenericAuthLogin(AuthLoginEvent event);
    Mono<Void> publishGenericAuthLogout(AuthLogoutEvent event);
}
