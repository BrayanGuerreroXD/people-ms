package co.com.pragma.peoplems.usecase.updateperson;

import co.com.pragma.peoplems.model.exception.GlobalExceptionEnum;
import co.com.pragma.peoplems.model.exception.NotFoundException;
import co.com.pragma.peoplems.model.person.Person;
import co.com.pragma.peoplems.model.person.gateways.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class UpdatePersonUseCase implements UpdatePersonService {

    private final PersonRepository personRepository;

    @Override
    @Transactional
    public Mono<Person> update(Long id, Person personData) {
        return personRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new NotFoundException(GlobalExceptionEnum.PERSON_NOT_FOUND)))
                .flatMap(existing -> {
                    Person updated = existing.toBuilder()
                            .name(personData.getName())
                            .age(personData.getAge())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    return personRepository.save(updated);
                });
    }
}