package io.github.otavioborgsm.rest.dto.comentario;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class EditComentarioDTO {

    @NotBlank
    private String conteudo;
}
