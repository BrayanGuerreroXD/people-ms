package co.com.pragma.peoplems.usecase.updateperson;

import co.com.pragma.peoplems.model.exception.NotFoundException;
import co.com.pragma.peoplems.model.person.Person;
import co.com.pragma.peoplems.model.person.gateways.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdatePersonUseCaseTest {

    @Mock
    PersonRepository personRepository;

    @InjectMocks
    UpdatePersonUseCase useCase;

    @Test
    void update_whenPersonExists_shouldUpdateNameAndAgeAndPreserveOtherFields() {
        Person existing = Person.builder()
                .id(1L).email("john@test.com").name("John").age(30).isAdmin(false).build();
        Person input = Person.builder().name("John Updated").age(31).build();

        when(personRepository.findById(1L)).thenReturn(Mono.just(existing));
        // Return the Person passed to save() so we can assert its updated fields
        when(personRepository.save(any(Person.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.update(1L, input))
                .assertNext(updated -> {
                    assertThat(updated.getName()).isEqualTo("John Updated");
                    assertThat(updated.getAge()).isEqualTo(31);
                    assertThat(updated.getEmail()).isEqualTo("john@test.com"); // unchanged
                    assertThat(updated.getId()).isEqualTo(1L);                 // unchanged
                    assertThat(updated.getUpdatedAt()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void update_whenPersonNotFound_shouldThrowNotFoundException() {
        when(personRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.update(99L, Person.builder().name("X").age(20).build()))
                .expectErrorMatches(err ->
                        err instanceof NotFoundException &&
                        err.getMessage().equals("Person not found"))
                .verify();
    }
}
