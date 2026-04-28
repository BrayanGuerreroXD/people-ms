package co.com.pragma.peoplems.usecase.login;

import co.com.pragma.peoplems.model.auth.Auth;
import co.com.pragma.peoplems.model.exception.UnauthorizedException;
import co.com.pragma.peoplems.model.person.Person;
import co.com.pragma.peoplems.model.person.gateways.PersonRepository;
import co.com.pragma.peoplems.model.security.EncryptionGateway;
import co.com.pragma.peoplems.model.security.JwtGateway;
import co.com.pragma.peoplems.model.security.LoggedUser;
import co.com.pragma.peoplems.usecase.getperson.GetPersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock private GetPersonService getPersonService;
    @Mock private co.com.pragma.peoplems.usecase.onlysaveperson.OnlySavePersonService onlySavePersonService;
    @Mock private EncryptionGateway encryptionGateway;
    @Mock private JwtGateway jwtGateway;

    @InjectMocks
    private LoginUseCase loginUseCase;

    private static final Person PERSON = Person.builder()
            .id(1L).email("user@test.com").password("hashed-pw")
            .isAdmin(false).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
            .build();

    @Test
    void login_whenPersonNotFound_throwsUnauthorizedException() {
        when(getPersonService.getByEmail("user@test.com")).thenReturn(Mono.empty());
        Auth auth = Auth.builder().email("user@test.com").password("raw-pw").build();

        StepVerifier.create(loginUseCase.login(auth))
                .expectError(UnauthorizedException.class)
                .verify();

        verify(encryptionGateway, never()).matches(anyString(), anyString());
    }

    @Test
    void login_whenPasswordMismatch_throwsUnauthorizedException() {
        when(getPersonService.getByEmail("user@test.com")).thenReturn(Mono.just(PERSON));
        when(encryptionGateway.matches("wrong-pw", "hashed-pw")).thenReturn(false);
        Auth auth = Auth.builder().email("user@test.com").password("wrong-pw").build();

        StepVerifier.create(loginUseCase.login(auth))
                .expectError(UnauthorizedException.class)
                .verify();

        verify(jwtGateway, never()).generateToken(any());
    }

    @Test
    void login_whenCredentialsValid_returnsAuthWithToken() {
        Person saved = PERSON.toBuilder().token("jwt-token").build();
        when(getPersonService.getByEmail("user@test.com")).thenReturn(Mono.just(PERSON));
        when(encryptionGateway.matches("raw-pw", "hashed-pw")).thenReturn(true);
        when(jwtGateway.generateToken(any(LoggedUser.class))).thenReturn("jwt-token");
        when(onlySavePersonService.save(any(Person.class))).thenReturn(Mono.just(saved));
        Auth auth = Auth.builder().email("user@test.com").password("raw-pw").build();

        StepVerifier.create(loginUseCase.login(auth))
                .expectNextMatches(result ->
                        "jwt-token".equals(result.getToken()) &&
                        "user@test.com".equals(result.getEmail()))
                .verifyComplete();
    }
}
