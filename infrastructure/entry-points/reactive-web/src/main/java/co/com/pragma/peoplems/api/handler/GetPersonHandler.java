package co.com.pragma.peoplems.api.handler;

import co.com.pragma.peoplems.api.dto.GenericResponseData;
import co.com.pragma.peoplems.api.mapper.PersonDtoMapper;
import co.com.pragma.peoplems.usecase.getperson.GetPersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GetPersonHandler {

    private final GetPersonService getPersonService;
    private final PersonDtoMapper personDtoMapper;

    public Mono<ServerResponse> getById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return getPersonService.getById(id)
                .map(personDtoMapper::toResponse)
                .map(GenericResponseData::of)
                .flatMap(body -> ServerResponse.ok().bodyValue(body));
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return getPersonService.getAll()
                .map(personDtoMapper::toResponse)
                .collectList()
                .map(GenericResponseData::of)
                .flatMap(body -> ServerResponse.ok().bodyValue(body));
    }
}
