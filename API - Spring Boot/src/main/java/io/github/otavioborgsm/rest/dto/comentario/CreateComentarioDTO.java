package io.github.otavioborgsm.rest.dto.comentario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateComentarioDTO {

    @NotBlank
    private String conteudo;

    @NotBlank
    private Long idPublicacao;

    @NotBlank
    private Long idUsuario;
}
