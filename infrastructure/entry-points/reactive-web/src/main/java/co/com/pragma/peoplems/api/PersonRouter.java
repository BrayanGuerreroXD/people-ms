package co.com.pragma.peoplems.api;

import co.com.pragma.peoplems.api.handler.CreatePersonHandler;
import co.com.pragma.peoplems.api.handler.GetPersonHandler;
import co.com.pragma.peoplems.api.handler.UpdatePersonHandler;
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
                    produces = MediaType.APPLICATION_JSON_VALUE),
            @RouterOperation(path = "/api/persons/{id}", method = RequestMethod.GET,
                    beanClass = GetPersonHandler.class, beanMethod = "getById",
                    produces = MediaType.APPLICATION_JSON_VALUE),
            @RouterOperation(path = "/api/persons", method = RequestMethod.GET,
                    beanClass = GetPersonHandler.class, beanMethod = "getAll",
                    produces = MediaType.APPLICATION_JSON_VALUE),
            @RouterOperation(path = "/api/persons/{id}", method = RequestMethod.PUT,
                    beanClass = UpdatePersonHandler.class, beanMethod = "handle",
                    produces = MediaType.APPLICATION_JSON_VALUE)
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
