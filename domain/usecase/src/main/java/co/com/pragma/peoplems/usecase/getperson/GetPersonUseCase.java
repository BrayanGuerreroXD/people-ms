package co.com.pragma.peoplems.usecase.getperson;

import co.com.pragma.peoplems.model.exception.ForbiddenException;
import co.com.pragma.peoplems.model.exception.GlobalExceptionEnum;
import co.com.pragma.peoplems.model.exception.NotFoundException;
import co.com.pragma.peoplems.model.person.Person;
import co.com.pragma.peoplems.model.person.gateways.PersonRepository;
import co.com.pragma.peoplems.model.security.UserContext;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetPersonUseCase implements GetPersonService {

    private final PersonRepository personRepository;
    private final UserContext userContext;

    @Override
    public Mono<Person> getById(Long id) {
        return userContext.currentUser()
                .flatMap(user -> {
                    if (Boolean.FALSE.equals(user.getIsAdmin())) {
                        return Mono.error(new ForbiddenException(GlobalExceptionEnum.FORBIDDEN_ACCESS));
                    }
                    return personRepository.findById(id)
                            .switchIfEmpty(Mono.error(
                                    new NotFoundException(GlobalExceptionEnum.PERSON_NOT_FOUND)));
                });
    }

    @Override
    public Mono<Person> getByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    @Override
    public Flux<Person> getAll() {
        return userContext.currentUser()
                .flatMapMany(user -> {
                    if (Boolean.FALSE.equals(user.getIsAdmin())) {
                        return Flux.error(new ForbiddenException(GlobalExceptionEnum.FORBIDDEN_ACCESS));
                    }
                    return personRepository.findAllNonAdmin();
                });
    }
}
