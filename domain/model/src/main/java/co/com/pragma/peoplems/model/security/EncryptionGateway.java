package co.com.pragma.peoplems.model.security;

public interface EncryptionGateway {
    String encode(String rawValue);
    boolean matches(String rawValue, String encodedValue);
}
