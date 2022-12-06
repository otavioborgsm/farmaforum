package io.github.otavioborgsm.rest.dto.usuario.recuperarSenha;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecuperarSenhaDTO {

    @Email(message = "E-mail inválido!")
    private String email;
}
