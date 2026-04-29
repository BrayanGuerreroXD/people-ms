package co.com.pragma.peoplems.model.security;

public interface JwtGateway {
    String generateToken(LoggedUser user);

    /**
     * Validates token signature and expiry. If requiredIsAdmin is non-null,
     * also checks LoggedUser.isAdmin == requiredIsAdmin; throws ForbiddenException otherwise.
     */
    LoggedUser validateToken(String token, Boolean requiredIsAdmin);

    int getExpirationSeconds();
}
