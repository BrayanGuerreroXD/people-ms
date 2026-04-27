package co.com.pragma.peoplems.usecase.login;

import co.com.pragma.peoplems.model.auth.Auth;
import co.com.pragma.peoplems.model.person.gateways.PersonRepository;
import co.com.pragma.peoplems.model.security.EncryptionGateway;
import co.com.pragma.peoplems.model.security.JwtGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginUseCase implements LoginService {

    private final PersonRepository personRepository;
    private final EncryptionGateway encryptionGateway;
    private final JwtGateway jwtGateway;

    @Override
    public Mono<Auth> login(Auth auth) {
        return Mono.empty(); // implemented in Task 3
    }
}
