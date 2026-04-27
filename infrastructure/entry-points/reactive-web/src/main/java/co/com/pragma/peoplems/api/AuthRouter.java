package co.com.pragma.peoplems.api;

import co.com.pragma.peoplems.api.handler.AuthHandler;
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
                    produces = MediaType.APPLICATION_JSON_VALUE),
            @RouterOperation(path = "/api/auth/logout", method = RequestMethod.POST,
                    beanClass = AuthHandler.class, beanMethod = "logout",
                    produces = MediaType.APPLICATION_JSON_VALUE)
    })
    @Bean
    public RouterFunction<ServerResponse> authRoutes(AuthHandler authHandler) {
        return route(POST("/api/auth/login"), authHandler::login)
                .andRoute(POST("/api/auth/logout"), authHandler::logout);
    }
}
