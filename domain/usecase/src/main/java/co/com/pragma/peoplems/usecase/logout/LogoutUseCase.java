package co.com.pragma.peoplems.usecase.logout;

import co.com.pragma.peoplems.model.event.AuthLogoutEvent;
import co.com.pragma.peoplems.model.event.gateways.EventGateway;
import co.com.pragma.peoplems.model.exception.GlobalExceptionEnum;
import co.com.pragma.peoplems.model.exception.UnauthorizedException;
import co.com.pragma.peoplems.model.person.Person;
import co.com.pragma.peoplems.model.security.JwtGateway;
import co.com.pragma.peoplems.model.security.LoggedUser;
import co.com.pragma.peoplems.usecase.getperson.GetPersonService;
import co.com.pragma.peoplems.usecase.onlysaveperson.OnlySavePersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.logging.Level;

@Log
@RequiredArgsConstructor
public class LogoutUseCase implements LogoutService {

    private final GetPersonService getPersonService;
    private final OnlySavePersonService onlySavePersonService;
    private final JwtGateway jwtGateway;
    private final EventGateway eventGateway;

    @Override
    public Mono<Void> logout(String token) {
        LoggedUser loggedUser = jwtGateway.validateToken(token, null);
        return getPersonService.getByEmail(loggedUser.getEmail())
                .switchIfEmpty(Mono.error(new UnauthorizedException(GlobalExceptionEnum.INVALID_TOKEN)))
                .flatMap(person -> {
                    if (!token.equals(person.getToken())) {
                        return Mono.error(new UnauthorizedException(GlobalExceptionEnum.INVALID_TOKEN));
                    }
                    Person updated = person.toBuilder()
                            .token(null)
                            .updatedAt(LocalDateTime.now())
                            .build();
                    return onlySavePersonService.save(updated)
                            .doOnSuccess(saved -> publishLogoutEvent(saved.getEmail(), token));
                })
                .then();
    }

    private void publishLogoutEvent(String email, String token) {
        AuthLogoutEvent event = AuthLogoutEvent.builder()
                .email(email)
                .token(token)
                .build();
        eventGateway.publishGenericAuthLogout(event)
                .subscribe(null, e -> log.log(Level.WARNING, "GenericAuthLogout event failed: {0}", e.getMessage()));
    }
}
