package io.github.otavioborgsm.rest.dto.comentario;

import io.github.otavioborgsm.rest.dto.usuario.InformacaoUsuarioDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetComentarioDTO {

    private Long id;
    private String conteudo;
    private Boolean refutado;
    private String dataCriacao;
    private Long publicacaoId;
    private InformacaoUsuarioDTO usuario;

}
