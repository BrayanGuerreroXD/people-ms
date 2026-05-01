package co.com.pragma.peoplems.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuthLoginEvent {
    private String name;
    private String email;
    private String token;
    private Integer expiresIn;
}
