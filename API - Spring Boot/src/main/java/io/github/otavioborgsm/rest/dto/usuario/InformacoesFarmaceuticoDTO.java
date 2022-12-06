package io.github.otavioborgsm.rest.dto.usuario;

import io.github.otavioborgsm.domain.enums.Estado;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InformacoesFarmaceuticoDTO extends InformacaoUsuarioDTO{

    private String crf;
    private Estado estado;

}
