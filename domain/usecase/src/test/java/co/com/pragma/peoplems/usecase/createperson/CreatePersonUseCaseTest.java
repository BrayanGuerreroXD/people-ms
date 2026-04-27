package co.com.pragma.peoplems.usecase.createperson;

import co.com.pragma.peoplems.model.exception.ConflictException;
import co.com.pragma.peoplems.model.exception.ForbiddenException;
import co.com.pragma.peoplems.model.person.Person;
import co.com.pragma.peoplems.model.person.gateways.PersonRepository;
import co.com.pragma.peoplems.model.security.EncryptionGateway;
import co.com.pragma.peoplems.model.security.LoggedUser;
import co.com.pragma.peoplems.model.security.UserContext;
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
class CreatePersonUseCaseTest {

    @Mock
    PersonRepository personRepository;

    @Mock
    EncryptionGateway encryptionGateway;

    @Mock
    private UserContext userContext;

    @InjectMocks
    CreatePersonUseCase useCase;

    private static final LoggedUser ADMIN_USER = LoggedUser.builder()
            .email("admin@test.com").isAdmin(true).build();
    private static final LoggedUser REGULAR_USER = LoggedUser.builder()
            .email("user@test.com").isAdmin(false).build();

    @Test
    void create_whenUserIsNotAdmin_throwsForbiddenException() {
        when(userContext.currentUser()).thenReturn(Mono.just(REGULAR_USER));
        Person input = Person.builder()
                .email("john@test.com")
                .password("secret")
                .name("John")
                .age(30)
                .build();

        StepVerifier.create(useCase.create(input))
                .expectError(ForbiddenException.class)
                .verify();
    }

    @Test
    void create_whenEmailDoesNotExist_shouldSavePersonWithEncodedPasswordAndDefaults() {
        Person input = Person.builder()
                .email("john@test.com")
                .password("secret")
                .name("John")
                .age(30)
                .build();

        when(userContext.currentUser()).thenReturn(Mono.just(ADMIN_USER));
        when(personRepository.findByEmail("john@test.com")).thenReturn(Mono.empty());
        when(encryptionGateway.encode("secret")).thenReturn("encoded");
        // Return the same Person passed to save() so we can assert its fields
        when(personRepository.save(any(Person.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.create(input))
                .assertNext(person -> {
                    assertThat(person.getPassword()).isEqualTo("encoded");
                    assertThat(person.getIsAdmin()).isFalse();
                    assertThat(person.getCreatedAt()).isNotNull();
                    assertThat(person.getUpdatedAt()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void create_whenEmailAlreadyExists_shouldThrowConflictException() {
        Person input = Person.builder()
                .email("existing@test.com")
                .password("secret")
                .name("John")
                .age(30)
                .build();

        Person existing = Person.builder().email("existing@test.com").build();

        when(userContext.currentUser()).thenReturn(Mono.just(ADMIN_USER));
        when(personRepository.findByEmail("existing@test.com")).thenReturn(Mono.just(existing));

        StepVerifier.create(useCase.create(input))
                .expectErrorMatches(err ->
                        err instanceof ConflictException &&
                        err.getMessage().equals("Email already exists"))
                .verify();
    }
}
