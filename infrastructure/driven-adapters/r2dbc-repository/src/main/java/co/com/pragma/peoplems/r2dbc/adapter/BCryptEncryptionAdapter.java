package co.com.pragma.peoplems.r2dbc.adapter;

import co.com.pragma.peoplems.model.security.EncryptionGateway;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptEncryptionAdapter implements EncryptionGateway {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Override
    public String encode(String rawValue) {
        return ENCODER.encode(rawValue);
    }

    @Override
    public boolean matches(String rawValue, String encodedValue) {
        return ENCODER.matches(rawValue, encodedValue);
    }
}
