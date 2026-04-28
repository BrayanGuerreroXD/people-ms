package co.com.pragma.peoplems.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePersonRequest {
    private String email;
    private String password;
    private String name;
    private Integer age;
}
