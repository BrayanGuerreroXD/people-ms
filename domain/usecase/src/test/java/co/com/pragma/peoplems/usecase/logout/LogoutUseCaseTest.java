package co.com.pragma.peoplems.usecase.logout;

import co.com.pragma.peoplems.model.exception.NotFoundException;
import co.com.pragma.peoplems.model.exception.UnauthorizedException;
import co.com.pragma.peoplems.model.person.Person;
import co.com.pragma.peoplems.model.person.gateways.PersonRepository;
import co.com.pragma.peoplems.model.security.JwtGateway;
import co.com.pragma.peoplems.model.security.LoggedUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogoutUseCaseTest {

    @Mock private PersonRepository personRepository;
    @Mock private JwtGateway jwtGateway;

    @InjectMocks
    private LogoutUseCase logoutUseCase;

    private static final LoggedUser LOGGED_USER = LoggedUser.builder()
            .email("user@test.com").isAdmin(false).build();

    private static final Person PERSON_WITH_TOKEN = Person.builder()
            .id(1L).email("user@test.com").token("valid-jwt")
            .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
            .build();

    @Test
    void logout_whenPersonNotFound_throwsNotFoundException() {
        when(jwtGateway.validateToken("valid-jwt", null)).thenReturn(LOGGED_USER);
        when(personRepository.findByEmail("user@test.com")).thenReturn(Mono.empty());

        StepVerifier.create(logoutUseCase.logout("valid-jwt"))
                .expectError(NotFoundException.class)
                .verify();

        verify(personRepository, never()).save(any());
    }

    @Test
    void logout_whenTokenMismatch_throwsUnauthorizedException() {
        when(jwtGateway.validateToken("different-jwt", null)).thenReturn(LOGGED_USER);
        when(personRepository.findByEmail("user@test.com")).thenReturn(Mono.just(PERSON_WITH_TOKEN));

        StepVerifier.create(logoutUseCase.logout("different-jwt"))
                .expectError(UnauthorizedException.class)
                .verify();

        verify(personRepository, never()).save(any());
    }

    @Test
    void logout_whenValid_clearsTokenAndCompletes() {
        Person nulledToken = PERSON_WITH_TOKEN.toBuilder().token(null).build();
        when(jwtGateway.validateToken("valid-jwt", null)).thenReturn(LOGGED_USER);
        when(personRepository.findByEmail("user@test.com")).thenReturn(Mono.just(PERSON_WITH_TOKEN));
        when(personRepository.save(any(Person.class))).thenReturn(Mono.just(nulledToken));

        StepVerifier.create(logoutUseCase.logout("valid-jwt"))
                .verifyComplete();
    }
}
