package co.com.pragma.peoplems.usecase.onlysaveperson;

import co.com.pragma.peoplems.model.person.Person;
import reactor.core.publisher.Mono;

public interface OnlySavePersonService {
    Mono<Person> save(Person person);
}
