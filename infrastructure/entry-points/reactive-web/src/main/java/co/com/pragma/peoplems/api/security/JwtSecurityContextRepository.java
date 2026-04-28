package co.com.pragma.peoplems.api.security;

import co.com.pragma.peoplems.model.security.JwtGateway;
import co.com.pragma.peoplems.model.security.LoggedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtSecurityContextRepository implements ServerSecurityContextRepository {

    private final JwtGateway jwtGateway;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.empty();
        }
        String token = authHeader.substring(7);
        try {
            LoggedUser loggedUser = jwtGateway.validateToken(token, null);
            var authority = Boolean.TRUE.equals(loggedUser.getIsAdmin())
                    ? new SimpleGrantedAuthority("ROLE_ADMIN")
                    : new SimpleGrantedAuthority("ROLE_USER");
            var auth = new UsernamePasswordAuthenticationToken(loggedUser, null, List.of(authority));
            return Mono.just(new SecurityContextImpl(auth));
        } catch (Exception e) {
            return Mono.empty();
        }
    }
}
