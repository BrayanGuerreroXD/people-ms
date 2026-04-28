package co.com.pragma.peoplems.usecase.onlysaveperson;

import co.com.pragma.peoplems.model.person.Person;
import co.com.pragma.peoplems.model.person.gateways.PersonRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class OnlySavePersonUseCase implements OnlySavePersonService {

    private final PersonRepository personRepository;

    @Override
    public Mono<Person> save(Person person) {
        return personRepository.save(person);
    }
}
