package io.github.otavioborgsm.rest.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {
    private Long id;
    private String login;
    private String token;
    private Long farmaceutico;
    private Boolean isAdmin;
}
