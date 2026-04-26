package co.com.pragma.peoplems.model.person.gateways;

import co.com.pragma.peoplems.model.person.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonRepository {
    Mono<Person> save(Person person);
    Mono<Person> findById(Long id);
    Mono<Person> findByEmail(String email);
    Flux<Person> findAllNonAdmin();
}
