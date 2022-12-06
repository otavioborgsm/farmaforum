package io.github.otavioborgsm.rest.dto.usuario.recuperarSenha;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NovaSenhaDTO {

    private String token;
    private String senha;

}
