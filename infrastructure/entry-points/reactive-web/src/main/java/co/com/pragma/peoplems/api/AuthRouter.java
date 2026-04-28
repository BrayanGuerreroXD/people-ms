package co.com.pragma.peoplems.api;

import co.com.pragma.peoplems.api.dto.LoginRequest;
import co.com.pragma.peoplems.api.dto.TokenResponse;
import co.com.pragma.peoplems.api.handler.AuthHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthRouter {

    @RouterOperations({
            @RouterOperation(path = "/api/auth/login", method = RequestMethod.POST,
                    beanClass = AuthHandler.class, beanMethod = "login",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    operation = @Operation(
                            operationId = "login",
                            summary = "Authenticate user and obtain JWT token",
                            requestBody = @RequestBody(required = true,
                                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = LoginRequest.class))),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Authentication successful",
                                            content = @Content(schema = @Schema(implementation = TokenResponse.class))),
                                    @ApiResponse(responseCode = "401", description = "Invalid credentials")
                            }
                    )),
            @RouterOperation(path = "/api/auth/logout", method = RequestMethod.POST,
                    beanClass = AuthHandler.class, beanMethod = "logout",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    operation = @Operation(
                            operationId = "logout",
                            summary = "Logout and invalidate JWT token",
                            parameters = {
                                    @Parameter(name = "Authorization", in = ParameterIn.HEADER, required = true,
                                            description = "Bearer JWT token",
                                            schema = @Schema(type = "string", example = "Bearer eyJhbGc..."))
                            },
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "Logout successful"),
                                    @ApiResponse(responseCode = "401", description = "Missing or invalid token")
                            }
                    ))
    })
    @Bean
    public RouterFunction<ServerResponse> authRoutes(AuthHandler authHandler) {
        return route(POST("/api/auth/login"), authHandler::login)
                .andRoute(POST("/api/auth/logout"), authHandler::logout);
    }
}
