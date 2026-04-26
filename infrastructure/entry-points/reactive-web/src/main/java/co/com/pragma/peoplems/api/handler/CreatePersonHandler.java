package co.com.pragma.peoplems.api.handler;

import co.com.pragma.peoplems.api.dto.CreatePersonRequest;
import co.com.pragma.peoplems.api.dto.GenericResponseData;
import co.com.pragma.peoplems.api.mapper.PersonDtoMapper;
import co.com.pragma.peoplems.usecase.createperson.CreatePersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CreatePersonHandler {

    private final CreatePersonService createPersonService;
    private final PersonDtoMapper personDtoMapper;

    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.bodyToMono(CreatePersonRequest.class)
                .map(personDtoMapper::toDomain)
                .flatMap(createPersonService::create)
                .map(personDtoMapper::toResponse)
                .map(GenericResponseData::of)
                .flatMap(body -> ServerResponse.status(HttpStatus.CREATED).bodyValue(body));
    }
}
