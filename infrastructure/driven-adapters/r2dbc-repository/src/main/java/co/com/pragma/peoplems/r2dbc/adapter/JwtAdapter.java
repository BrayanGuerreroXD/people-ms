package co.com.pragma.peoplems.r2dbc.adapter;

import co.com.pragma.peoplems.model.exception.ForbiddenException;
import co.com.pragma.peoplems.model.exception.GlobalExceptionEnum;
import co.com.pragma.peoplems.model.exception.UnauthorizedException;
import co.com.pragma.peoplems.model.security.JwtGateway;
import co.com.pragma.peoplems.model.security.LoggedUser;
import co.com.pragma.peoplems.r2dbc.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtAdapter implements JwtGateway {

    private final JwtProperties jwtProperties;

    @Override
    public String generateToken(LoggedUser user) {
        long expirationMs = jwtProperties.getExpirationHours() * 3_600_000L;
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("isAdmin", user.getIsAdmin())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(buildKey())
                .compact();
    }

    @Override
    public LoggedUser validateToken(String token, Boolean requiredIsAdmin) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(buildKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String email = claims.getSubject();
            Boolean isAdmin = claims.get("isAdmin", Boolean.class);
            LoggedUser loggedUser = LoggedUser.builder().email(email).isAdmin(isAdmin).build();

            if (requiredIsAdmin != null && !requiredIsAdmin.equals(isAdmin)) {
                throw new ForbiddenException(GlobalExceptionEnum.FORBIDDEN_ACCESS);
            }
            return loggedUser;
        } catch (ForbiddenException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedException(GlobalExceptionEnum.INVALID_TOKEN);
        }
    }

    private SecretKey buildKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}
