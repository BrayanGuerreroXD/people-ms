package co.com.pragma.peoplems.api.exception;

import co.com.pragma.peoplems.api.dto.GenericResponseData;
import co.com.pragma.peoplems.model.exception.BadRequestException;
import co.com.pragma.peoplems.model.exception.ConflictException;
import co.com.pragma.peoplems.model.exception.ForbiddenException;
import co.com.pragma.peoplems.model.exception.InternalServerErrorException;
import co.com.pragma.peoplems.model.exception.NotFoundException;
import co.com.pragma.peoplems.model.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

@Component
@Order(-2)
@RequiredArgsConstructor
public class GlobalExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = resolveStatus(ex);
        GenericResponseData<ErrorData> body = buildErrorBody(ex);

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return Mono.fromCallable(() -> objectMapper.writeValueAsBytes(body))
            .flatMap(bytes -> exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes))));
    }

    private HttpStatus resolveStatus(Throwable ex) {
        if (ex instanceof BadRequestException) return HttpStatus.BAD_REQUEST;
        if (ex instanceof NotFoundException) return HttpStatus.NOT_FOUND;
        if (ex instanceof ForbiddenException) return HttpStatus.FORBIDDEN;
        if (ex instanceof UnauthorizedException) return HttpStatus.UNAUTHORIZED;
        if (ex instanceof ConflictException) return HttpStatus.CONFLICT;
        if (ex instanceof InternalServerErrorException) return HttpStatus.INTERNAL_SERVER_ERROR;
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private GenericResponseData<ErrorData> buildErrorBody(Throwable ex) {
        if (ex instanceof BadRequestException e) return GenericResponseData.of(ErrorData.of(e.getError()));
        if (ex instanceof NotFoundException e) return GenericResponseData.of(ErrorData.of(e.getError()));
        if (ex instanceof ForbiddenException e) return GenericResponseData.of(ErrorData.of(e.getError()));
        if (ex instanceof UnauthorizedException e) return GenericResponseData.of(ErrorData.of(e.getError()));
        if (ex instanceof ConflictException e) return GenericResponseData.of(ErrorData.of(e.getError()));
        if (ex instanceof InternalServerErrorException e) return GenericResponseData.of(ErrorData.of(e.getError()));
        return GenericResponseData.of(new ErrorData("UNKNOWN", "Internal Server Error", "An unexpected error occurred"));
    }
}
