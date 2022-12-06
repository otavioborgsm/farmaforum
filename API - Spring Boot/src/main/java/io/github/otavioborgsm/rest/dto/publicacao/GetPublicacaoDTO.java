package io.github.otavioborgsm.rest.dto.publicacao;

import io.github.otavioborgsm.rest.dto.comentario.GetComentarioDTO;
import io.github.otavioborgsm.rest.dto.farmacoPublicacao.GetFarmacoPublicacaoDTO;
import io.github.otavioborgsm.rest.dto.usuario.InformacaoUsuarioDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPublicacaoDTO {

    private Long id;

    private String titulo;

    private String conteudo;

    private String dataCriacao;

    private InformacaoUsuarioDTO usuario;

    private List<GetComentarioDTO> comentarios;

    private List<GetFarmacoPublicacaoDTO> farmacos;

}

