package io.github.otavioborgsm.rest.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUsuarioDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String sobrenome;

    @NotEmpty(message = "{campo.login.obrigatorio}")
    @Email(message = "E-mail não é valido.")
    private String login;

    @NotEmpty(message = "{campo.senha.obrigatorio}")
    private String senha;

}
