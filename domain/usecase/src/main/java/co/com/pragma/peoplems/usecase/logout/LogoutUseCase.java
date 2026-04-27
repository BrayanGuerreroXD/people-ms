package co.com.pragma.peoplems.usecase.logout;

import co.com.pragma.peoplems.model.person.gateways.PersonRepository;
import co.com.pragma.peoplems.model.security.JwtGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LogoutUseCase implements LogoutService {

    private final PersonRepository personRepository;
    private final JwtGateway jwtGateway;

    @Override
    public Mono<Void> logout(String token) {
        return Mono.empty(); // implemented in Task 4
    }
}
