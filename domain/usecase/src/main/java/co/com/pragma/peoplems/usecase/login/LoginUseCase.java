package co.com.pragma.peoplems.usecase.login;

import co.com.pragma.peoplems.model.auth.Auth;
import co.com.pragma.peoplems.model.exception.GlobalExceptionEnum;
import co.com.pragma.peoplems.model.exception.NotFoundException;
import co.com.pragma.peoplems.model.exception.UnauthorizedException;
import co.com.pragma.peoplems.model.person.Person;
import co.com.pragma.peoplems.model.person.gateways.PersonRepository;
import co.com.pragma.peoplems.model.security.EncryptionGateway;
import co.com.pragma.peoplems.model.security.JwtGateway;
import co.com.pragma.peoplems.model.security.LoggedUser;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class LoginUseCase implements LoginService {

    private final PersonRepository personRepository;
    private final EncryptionGateway encryptionGateway;
    private final JwtGateway jwtGateway;

    @Override
    public Mono<Auth> login(Auth auth) {
        return personRepository.findByEmail(auth.getEmail())
                .switchIfEmpty(Mono.error(new NotFoundException(GlobalExceptionEnum.PERSON_NOT_FOUND)))
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
                    return personRepository.save(updated)
                            .map(saved -> Auth.builder()
                                    .email(saved.getEmail())
                                    .token(saved.getToken())
                                    .build());
                });
    }
}
