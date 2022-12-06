package io.github.otavioborgsm.rest.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditUsuarioDTO {
    private String nome;

    private String sobrenome;
}
