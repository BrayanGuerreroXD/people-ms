package co.com.pragma.peoplems.r2dbc.repository;

import co.com.pragma.peoplems.r2dbc.entity.PersonEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonR2dbcRepository extends ReactiveCrudRepository<PersonEntity, Long> {
    Mono<PersonEntity> findByEmail(String email);
    Flux<PersonEntity> findAllByIsAdminFalse();
}
