package co.com.pragma.peoplems.usecase.createperson;

import co.com.pragma.peoplems.model.person.Person;
import reactor.core.publisher.Mono;

public interface CreatePersonService {
    Mono<Person> create(Person person);
}
