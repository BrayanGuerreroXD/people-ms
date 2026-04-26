package co.com.pragma.peoplems.usecase.updateperson;

import co.com.pragma.peoplems.model.person.Person;
import reactor.core.publisher.Mono;

public interface UpdatePersonService {
    Mono<Person> update(Long id, Person personData);
}
