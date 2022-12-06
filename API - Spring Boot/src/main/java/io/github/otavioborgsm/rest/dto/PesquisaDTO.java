package io.github.otavioborgsm.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PesquisaDTO {

    @NotEmpty(message = "A pesquisa não pode ser vazia.")
    String pesquisa;
}
