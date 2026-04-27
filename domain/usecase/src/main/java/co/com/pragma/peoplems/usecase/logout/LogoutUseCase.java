package co.com.pragma.peoplems.usecase.logout;

import co.com.pragma.peoplems.model.exception.GlobalExceptionEnum;
import co.com.pragma.peoplems.model.exception.NotFoundException;
import co.com.pragma.peoplems.model.exception.UnauthorizedException;
import co.com.pragma.peoplems.model.person.Person;
import co.com.pragma.peoplems.model.person.gateways.PersonRepository;
import co.com.pragma.peoplems.model.security.JwtGateway;
import co.com.pragma.peoplems.model.security.LoggedUser;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class LogoutUseCase implements LogoutService {

    private final PersonRepository personRepository;
    private final JwtGateway jwtGateway;

    @Override
    public Mono<Void> logout(String token) {
        LoggedUser loggedUser = jwtGateway.validateToken(token, null);
        return personRepository.findByEmail(loggedUser.getEmail())
                .switchIfEmpty(Mono.error(new NotFoundException(GlobalExceptionEnum.PERSON_NOT_FOUND)))
                .flatMap(person -> {
                    if (!token.equals(person.getToken())) {
                        return Mono.error(new UnauthorizedException(GlobalExceptionEnum.INVALID_TOKEN));
                    }
                    Person updated = person.toBuilder()
                            .token(null)
                            .updatedAt(LocalDateTime.now())
                            .build();
                    return personRepository.save(updated);
                })
                .then();
    }
}
