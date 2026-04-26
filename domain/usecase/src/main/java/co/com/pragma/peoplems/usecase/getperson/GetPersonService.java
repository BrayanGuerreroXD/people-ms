package co.com.pragma.peoplems.usecase.getperson;

import co.com.pragma.peoplems.model.person.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetPersonService {
    Mono<Person> getById(Long id);
    Mono<Person> getByEmail(String email);
    Flux<Person> getAll();
}
