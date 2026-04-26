package co.com.pragma.peoplems.r2dbc.adapter;

import co.com.pragma.peoplems.model.person.Person;
import co.com.pragma.peoplems.model.person.gateways.PersonRepository;
import co.com.pragma.peoplems.r2dbc.mapper.PersonEntityMapper;
import co.com.pragma.peoplems.r2dbc.repository.PersonR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PersonRepositoryAdapter implements PersonRepository {

    private final PersonR2dbcRepository repository;
    private final PersonEntityMapper mapper;

    @Override
    public Mono<Person> save(Person person) {
        return repository.save(mapper.toEntity(person))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Person> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Person> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Person> findAllNonAdmin() {
        return repository.findAllByIsAdminFalse()
                .map(mapper::toDomain);
    }
}
