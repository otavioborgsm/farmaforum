package io.github.otavioborgsm.rest.dto.farmacoPublicacao;

import io.github.otavioborgsm.domain.entity.Farmaco;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetFarmacoPublicacaoDTO {

    private Long id;
    private Long idPublicacao;
    private Farmaco farmaco;

}
