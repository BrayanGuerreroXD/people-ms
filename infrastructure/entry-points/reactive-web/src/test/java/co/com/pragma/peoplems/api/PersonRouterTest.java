package co.com.pragma.peoplems.api;

import co.com.pragma.peoplems.api.dto.CreatePersonRequest;
import co.com.pragma.peoplems.api.dto.PersonResponse;
import co.com.pragma.peoplems.api.dto.UpdatePersonRequest;
import co.com.pragma.peoplems.api.exception.GlobalExceptionHandler;
import co.com.pragma.peoplems.api.handler.CreatePersonHandler;
import co.com.pragma.peoplems.api.handler.GetPersonHandler;
import co.com.pragma.peoplems.api.handler.UpdatePersonHandler;
import co.com.pragma.peoplems.api.mapper.PersonDtoMapper;
import co.com.pragma.peoplems.model.exception.ConflictException;
import co.com.pragma.peoplems.model.exception.GlobalExceptionEnum;
import co.com.pragma.peoplems.model.exception.NotFoundException;
import co.com.pragma.peoplems.model.person.Person;
import co.com.pragma.peoplems.usecase.createperson.CreatePersonService;
import co.com.pragma.peoplems.usecase.getperson.GetPersonService;
import co.com.pragma.peoplems.usecase.updateperson.UpdatePersonService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.HttpHandlerConnector;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonRouterTest {

    @Mock CreatePersonService createPersonService;
    @Mock GetPersonService getPersonService;
    @Mock UpdatePersonService updatePersonService;
    @Mock PersonDtoMapper personDtoMapper;

    private WebTestClient client;

    @BeforeEach
    void setUp() {
        CreatePersonHandler createHandler = new CreatePersonHandler(createPersonService, personDtoMapper);
        GetPersonHandler getHandler = new GetPersonHandler(getPersonService, personDtoMapper);
        UpdatePersonHandler updateHandler = new UpdatePersonHandler(updatePersonService, personDtoMapper);

        RouterFunction<ServerResponse> routes = new PersonRouter()
                .personRoutes(createHandler, getHandler, updateHandler);

        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler(new ObjectMapper());

        var httpHandler = WebHttpHandlerBuilder
                .webHandler(RouterFunctions.toWebHandler(routes))
                .exceptionHandler(exceptionHandler)
                .build();

        client = WebTestClient.bindToServer(new HttpHandlerConnector(httpHandler)).build();
    }

    @Test
    void POST_persons_returns201_withPersonResponse() {
        LocalDateTime now = LocalDateTime.now();
        Person domain = Person.builder().email("john@test.com").name("John").age(30).build();
        Person saved = domain.toBuilder().id(1L).build();
        PersonResponse response = PersonResponse.builder()
                .id(1L).email("john@test.com").name("John").age(30)
                .createdAt(now).updatedAt(now).build();

        when(personDtoMapper.toDomain(any(CreatePersonRequest.class))).thenReturn(domain);
        when(createPersonService.create(any(Person.class))).thenReturn(Mono.just(saved));
        when(personDtoMapper.toResponse(any(Person.class))).thenReturn(response);

        client.post().uri("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreatePersonRequest("john@test.com", "secret", "John", 30))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.data.id").isEqualTo(1)
                .jsonPath("$.data.email").isEqualTo("john@test.com")
                .jsonPath("$.data.name").isEqualTo("John")
                .jsonPath("$.data.password").doesNotExist()
                .jsonPath("$.data.isAdmin").doesNotExist();
    }

    @Test
    void GET_persons_byId_returns200_withPersonResponse() {
        LocalDateTime now = LocalDateTime.now();
        Person person = Person.builder().id(1L).email("john@test.com").name("John").age(30).build();
        PersonResponse response = PersonResponse.builder()
                .id(1L).email("john@test.com").name("John").age(30)
                .createdAt(now).updatedAt(now).build();

        when(getPersonService.getById(1L)).thenReturn(Mono.just(person));
        when(personDtoMapper.toResponse(any(Person.class))).thenReturn(response);

        client.get().uri("/api/persons/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.id").isEqualTo(1)
                .jsonPath("$.data.email").isEqualTo("john@test.com");
    }

    @Test
    void GET_persons_returns200_withListOfPersons() {
        LocalDateTime now = LocalDateTime.now();
        Person alice = Person.builder().id(1L).name("Alice").email("alice@test.com").age(25).build();
        Person bob = Person.builder().id(2L).name("Bob").email("bob@test.com").age(28).build();
        PersonResponse r1 = PersonResponse.builder()
                .id(1L).email("alice@test.com").name("Alice").age(25)
                .createdAt(now).updatedAt(now).build();
        PersonResponse r2 = PersonResponse.builder()
                .id(2L).email("bob@test.com").name("Bob").age(28)
                .createdAt(now).updatedAt(now).build();

        when(getPersonService.getAll()).thenReturn(Flux.just(alice, bob));
        when(personDtoMapper.toResponse(alice)).thenReturn(r1);
        when(personDtoMapper.toResponse(bob)).thenReturn(r2);

        client.get().uri("/api/persons")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data").isArray()
                .jsonPath("$.data[0].id").isEqualTo(1)
                .jsonPath("$.data[1].id").isEqualTo(2);
    }

    @Test
    void PUT_persons_byId_returns200_withUpdatedPerson() {
        LocalDateTime now = LocalDateTime.now();
        Person input = Person.builder().name("John Updated").age(31).build();
        Person updated = Person.builder()
                .id(1L).email("john@test.com").name("John Updated").age(31).build();
        PersonResponse response = PersonResponse.builder()
                .id(1L).email("john@test.com").name("John Updated").age(31)
                .createdAt(now).updatedAt(now).build();

        when(personDtoMapper.toDomain(any(UpdatePersonRequest.class))).thenReturn(input);
        when(updatePersonService.update(eq(1L), any(Person.class))).thenReturn(Mono.just(updated));
        when(personDtoMapper.toResponse(any(Person.class))).thenReturn(response);

        client.put().uri("/api/persons/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdatePersonRequest("John Updated", 31))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.name").isEqualTo("John Updated")
                .jsonPath("$.data.age").isEqualTo(31);
    }

    @Test
    void GET_persons_byId_returns404_whenPersonNotFound() {
        when(getPersonService.getById(99L))
                .thenReturn(Mono.error(new NotFoundException(GlobalExceptionEnum.PERSON_NOT_FOUND)));

        client.get().uri("/api/persons/99")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.data.errorCode").isEqualTo("PERSON_NOT_FOUND")
                .jsonPath("$.data.message").isEqualTo("Person not found");
    }

    @Test
    void POST_persons_returns409_whenEmailAlreadyExists() {
        Person domain = Person.builder()
                .email("dup@test.com").password("s").name("X").age(20).build();

        when(personDtoMapper.toDomain(any(CreatePersonRequest.class))).thenReturn(domain);
        when(createPersonService.create(any(Person.class)))
                .thenReturn(Mono.error(new ConflictException(GlobalExceptionEnum.EMAIL_ALREADY_EXISTS)));

        client.post().uri("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreatePersonRequest("dup@test.com", "s", "X", 20))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath("$.data.errorCode").isEqualTo("EMAIL_ALREADY_EXISTS")
                .jsonPath("$.data.message").isEqualTo("Email already exists");
    }
}
