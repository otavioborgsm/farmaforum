package io.github.otavioborgsm.rest.dto.publicacao;

import io.github.otavioborgsm.rest.dto.farmacoPublicacao.GetFarmacoPublicacaoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePublicacaoDTO {

    @NotEmpty(message = "Campo Titulo é obrigatório")
    private String titulo;

    @NotEmpty(message = "Campo Conteúdo é obrigatório")
    private String conteudo;

    private Long idUsuario;

    private List<GetFarmacoPublicacaoDTO> farmacos;

}
