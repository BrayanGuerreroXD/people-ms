package co.com.pragma.peoplems.usecase.getperson;

import co.com.pragma.peoplems.model.exception.ForbiddenException;
import co.com.pragma.peoplems.model.exception.NotFoundException;
import co.com.pragma.peoplems.model.person.Person;
import co.com.pragma.peoplems.model.person.gateways.PersonRepository;
import co.com.pragma.peoplems.model.security.LoggedUser;
import co.com.pragma.peoplems.model.security.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPersonUseCaseTest {

    @Mock
    PersonRepository personRepository;

    @Mock
    private UserContext userContext;

    @InjectMocks
    GetPersonUseCase useCase;

    private static final LoggedUser ADMIN_USER = LoggedUser.builder()
            .email("admin@test.com").isAdmin(true).build();
    private static final LoggedUser REGULAR_USER = LoggedUser.builder()
            .email("user@test.com").isAdmin(false).build();

    @Test
    void getById_whenPersonExists_shouldReturnPerson() {
        Person person = Person.builder()
                .id(1L).name("John").email("john@test.com").age(30).build();

        when(userContext.currentUser()).thenReturn(Mono.just(ADMIN_USER));
        when(personRepository.findById(1L)).thenReturn(Mono.just(person));

        StepVerifier.create(useCase.getById(1L))
                .assertNext(p -> {
                    assertThat(p.getId()).isEqualTo(1L);
                    assertThat(p.getName()).isEqualTo("John");
                })
                .verifyComplete();
    }

    @Test
    void getById_whenPersonNotFound_shouldThrowNotFoundException() {
        when(userContext.currentUser()).thenReturn(Mono.just(ADMIN_USER));
        when(personRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.getById(99L))
                .expectErrorMatches(err ->
                        err instanceof NotFoundException &&
                        err.getMessage().equals("Person not found"))
                .verify();
    }

    @Test
    void getByEmail_shouldReturnPersonFromRepository() {
        Person person = Person.builder()
                .email("john@test.com").name("John").build();

        when(personRepository.findByEmail("john@test.com")).thenReturn(Mono.just(person));

        StepVerifier.create(useCase.getByEmail("john@test.com"))
                .assertNext(p -> assertThat(p.getEmail()).isEqualTo("john@test.com"))
                .verifyComplete();
    }

    @Test
    void getAll_shouldReturnOnlyNonAdminPersonsFromRepository() {
        Person alice = Person.builder().id(1L).name("Alice").isAdmin(false).build();
        Person bob = Person.builder().id(2L).name("Bob").isAdmin(false).build();

        when(userContext.currentUser()).thenReturn(Mono.just(ADMIN_USER));
        when(personRepository.findAllNonAdmin()).thenReturn(Flux.just(alice, bob));

        StepVerifier.create(useCase.getAll())
                .assertNext(p -> assertThat(p.getName()).isEqualTo("Alice"))
                .assertNext(p -> assertThat(p.getName()).isEqualTo("Bob"))
                .verifyComplete();
    }

    @Test
    void getById_whenUserIsNotAdmin_throwsForbiddenException() {
        when(userContext.currentUser()).thenReturn(Mono.just(REGULAR_USER));

        StepVerifier.create(useCase.getById(1L))
                .expectError(ForbiddenException.class)
                .verify();
    }

    @Test
    void getById_whenUserIsAdmin_returnsPersonSuccessfully() {
        Person person = Person.builder()
                .id(1L).name("John").email("john@test.com").age(30).build();
        when(userContext.currentUser()).thenReturn(Mono.just(ADMIN_USER));
        when(personRepository.findById(1L)).thenReturn(Mono.just(person));

        StepVerifier.create(useCase.getById(1L))
                .expectNext(person)
                .verifyComplete();
    }

    @Test
    void getAll_whenUserIsNotAdmin_throwsForbiddenException() {
        when(userContext.currentUser()).thenReturn(Mono.just(REGULAR_USER));

        StepVerifier.create(useCase.getAll())
                .expectError(ForbiddenException.class)
                .verify();
    }

    @Test
    void getAll_whenUserIsAdmin_returnsAllPersons() {
        Person alice = Person.builder().id(1L).name("Alice").isAdmin(false).build();
        when(userContext.currentUser()).thenReturn(Mono.just(ADMIN_USER));
        when(personRepository.findAllNonAdmin()).thenReturn(Flux.just(alice));

        StepVerifier.create(useCase.getAll())
                .expectNext(alice)
                .verifyComplete();
    }
}
