package co.com.pragma.peoplems.api.security;

import co.com.pragma.peoplems.model.security.LoggedUser;
import co.com.pragma.peoplems.model.security.UserContext;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ReactiveUserContext implements UserContext {

    @Override
    public Mono<LoggedUser> currentUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> (LoggedUser) ctx.getAuthentication().getPrincipal());
    }
}
