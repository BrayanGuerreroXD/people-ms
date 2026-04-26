package co.com.pragma.peoplems.api;

import co.com.pragma.peoplems.api.handler.CreatePersonHandler;
import co.com.pragma.peoplems.api.handler.GetPersonHandler;
import co.com.pragma.peoplems.api.handler.UpdatePersonHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PersonRouter {

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
