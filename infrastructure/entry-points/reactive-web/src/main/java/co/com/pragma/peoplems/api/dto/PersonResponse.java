package co.com.pragma.peoplems.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonResponse {
    private Long id;
    private String email;
    private String name;
    private Integer age;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
