package io.github.otavioborgsm.rest.dto.publicacao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditPublicacaoDTO {
    private String titulo;
    private String conteudo;
}
