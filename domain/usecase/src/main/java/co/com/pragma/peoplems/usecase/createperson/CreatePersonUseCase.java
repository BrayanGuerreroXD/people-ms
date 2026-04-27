package co.com.pragma.peoplems.usecase.createperson;

import co.com.pragma.peoplems.model.exception.ConflictException;
import co.com.pragma.peoplems.model.exception.ForbiddenException;
import co.com.pragma.peoplems.model.exception.GlobalExceptionEnum;
import co.com.pragma.peoplems.model.person.Person;
import co.com.pragma.peoplems.model.person.gateways.PersonRepository;
import co.com.pragma.peoplems.model.security.EncryptionGateway;
import co.com.pragma.peoplems.model.security.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CreatePersonUseCase implements CreatePersonService {

    private final PersonRepository personRepository;
    private final EncryptionGateway encryptionGateway;
    private final UserContext userContext;

    @Override
    @Transactional
    public Mono<Person> create(Person person) {
        return userContext.currentUser()
                .flatMap(user -> {
                    if (Boolean.FALSE.equals(user.getIsAdmin())) {
                        return Mono.error(new ForbiddenException(GlobalExceptionEnum.FORBIDDEN_ACCESS));
                    }
                    return personRepository.findByEmail(person.getEmail())
                            .flatMap(existing -> Mono.<Person>error(
                                    new ConflictException(GlobalExceptionEnum.EMAIL_ALREADY_EXISTS)))
                            .switchIfEmpty(Mono.defer(() -> {
                                LocalDateTime now = LocalDateTime.now();
                                Person toSave = person.toBuilder()
                                        .password(encryptionGateway.encode(person.getPassword()))
                                        .isAdmin(false)
                                        .createdAt(now)
                                        .updatedAt(now)
                                        .build();
                                return personRepository.save(toSave);
                            }));
                });
    }
}
