package co.com.pragma.peoplems.usecase.login;

import co.com.pragma.peoplems.model.auth.Auth;
import co.com.pragma.peoplems.model.event.AuthLoginEvent;
import co.com.pragma.peoplems.model.event.gateways.EventGateway;
import co.com.pragma.peoplems.model.exception.GlobalExceptionEnum;
import co.com.pragma.peoplems.model.exception.UnauthorizedException;
import co.com.pragma.peoplems.model.person.Person;
import co.com.pragma.peoplems.model.security.EncryptionGateway;
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
public class LoginUseCase implements LoginService {

    private final GetPersonService getPersonService;
    private final OnlySavePersonService onlySavePersonService;
    private final EncryptionGateway encryptionGateway;
    private final JwtGateway jwtGateway;
    private final EventGateway eventGateway;

    @Override
    public Mono<Auth> login(Auth auth) {
        return getPersonService.getByEmail(auth.getEmail())
                .switchIfEmpty(Mono.error(new UnauthorizedException(GlobalExceptionEnum.INVALID_CREDENTIALS)))
                .flatMap(person -> {
                    if (!encryptionGateway.matches(auth.getPassword(), person.getPassword())) {
                        return Mono.error(new UnauthorizedException(GlobalExceptionEnum.INVALID_CREDENTIALS));
                    }
                    LoggedUser loggedUser = LoggedUser.builder()
                            .email(person.getEmail())
                            .isAdmin(person.getIsAdmin())
                            .build();
                    String token = jwtGateway.generateToken(loggedUser);
                    Person updated = person.toBuilder()
                            .token(token)
                            .updatedAt(LocalDateTime.now())
                            .build();
                    return onlySavePersonService.save(updated)
                            .map(saved -> Auth.builder()
                                    .email(saved.getEmail())
                                    .token(saved.getToken())
                                    .build())
                            .doOnSuccess(result -> publishLoginEvents(person.getIsAdmin(), result));
                });
    }

    private void publishLoginEvents(Boolean isAdmin, Auth auth) {
        AuthLoginEvent event = AuthLoginEvent.builder()
                .email(auth.getEmail())
                .token(auth.getToken())
                .expiresIn(jwtGateway.getExpirationSeconds())
                .build();
        if (Boolean.TRUE.equals(isAdmin)) {
            eventGateway.publishAuthLoginAdmin(event)
                    .subscribe(null, e -> log.log(Level.WARNING, "AuthLoginAdmin event failed: {0}", e.getMessage()));
        }
        eventGateway.publishGenericAuthLogin(event)
                .subscribe(null, e -> log.log(Level.WARNING, "GenericAuthLogin event failed: {0}", e.getMessage()));
    }
}
