package co.com.pragma.peoplems.api.handler;

import co.com.pragma.peoplems.api.dto.GenericResponseData;
import co.com.pragma.peoplems.api.dto.UpdatePersonRequest;
import co.com.pragma.peoplems.api.mapper.PersonDtoMapper;
import co.com.pragma.peoplems.usecase.updateperson.UpdatePersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UpdatePersonHandler {

    private final UpdatePersonService updatePersonService;
    private final PersonDtoMapper personDtoMapper;

    public Mono<ServerResponse> handle(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return request.bodyToMono(UpdatePersonRequest.class)
                .map(personDtoMapper::toDomain)
                .flatMap(person -> updatePersonService.update(id, person))
                .map(personDtoMapper::toResponse)
                .map(GenericResponseData::of)
                .flatMap(body -> ServerResponse.ok().bodyValue(body));
    }
}
