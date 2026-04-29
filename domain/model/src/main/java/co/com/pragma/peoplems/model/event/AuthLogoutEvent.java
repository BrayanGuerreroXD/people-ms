package co.com.pragma.peoplems.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuthLogoutEvent {
    private String email;
    private String token;
}
