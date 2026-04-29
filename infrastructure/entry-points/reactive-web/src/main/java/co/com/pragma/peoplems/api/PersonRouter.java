package co.com.pragma.peoplems.api;

import co.com.pragma.peoplems.api.dto.CreatePersonRequest;
import co.com.pragma.peoplems.api.dto.PersonResponse;
import co.com.pragma.peoplems.api.dto.UpdatePersonRequest;
import co.com.pragma.peoplems.api.handler.CreatePersonHandler;
import co.com.pragma.peoplems.api.handler.GetPersonHandler;
import co.com.pragma.peoplems.api.handler.UpdatePersonHandler;
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

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PersonRouter {

    @RouterOperations({
            @RouterOperation(path = "/api/persons", method = RequestMethod.POST,
                    beanClass = CreatePersonHandler.class, beanMethod = "handle",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    operation = @Operation(
                            operationId = "createPerson",
                            summary = "Create a new person",
                            requestBody = @RequestBody(required = true,
                                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = CreatePersonRequest.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Person created successfully",
                                            content = @Content(schema = @Schema(implementation = PersonResponse.class))),
                                    @ApiResponse(responseCode = "409", description = "Email already registered")
                            }
                    )),
            @RouterOperation(path = "/api/persons/{id}", method = RequestMethod.GET,
                    beanClass = GetPersonHandler.class, beanMethod = "getById",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    operation = @Operation(
                            operationId = "getPersonById",
                            summary = "Get person by ID",
                            parameters = {
                                    @Parameter(name = "id", in = ParameterIn.PATH, required = true,
                                            schema = @Schema(type = "integer", format = "int64"))
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Person found",
                                            content = @Content(schema = @Schema(implementation = PersonResponse.class))),
                                    @ApiResponse(responseCode = "404", description = "Person not found")
                            }
                    )),
            @RouterOperation(path = "/api/persons", method = RequestMethod.GET,
                    beanClass = GetPersonHandler.class, beanMethod = "getAll",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    operation = @Operation(
                            operationId = "getAllPersons",
                            summary = "Get all persons",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "List of persons",
                                            content = @Content(schema = @Schema(implementation = PersonResponse.class)))
                            }
                    )),
            @RouterOperation(path = "/api/persons/{id}", method = RequestMethod.PUT,
                    beanClass = UpdatePersonHandler.class, beanMethod = "handle",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    operation = @Operation(
                            operationId = "updatePerson",
                            summary = "Update an existing person",
                            parameters = {
                                    @Parameter(name = "id", in = ParameterIn.PATH, required = true,
                                            schema = @Schema(type = "integer", format = "int64"))
                            },
                            requestBody = @RequestBody(required = true,
                                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = UpdatePersonRequest.class))),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Person updated successfully",
                                            content = @Content(schema = @Schema(implementation = PersonResponse.class))),
                                    @ApiResponse(responseCode = "404", description = "Person not found")
                            }
                    ))
    })
    @Bean
    public RouterFunction<ServerResponse> personRoutes(
            CreatePersonHandler createHandler,
            GetPersonHandler getHandler,
            UpdatePersonHandler updateHandler) {
        return route(POST("/api/persons"), createHandler::handle)
                .andRoute(GET("/api/persons/{id}"), getHandler::getById)
                .andRoute(GET("/api/persons"), getHandler::getAll)
                .andRoute(PUT("/api/persons/{id}"), updateHandler::handle);
    }
}
