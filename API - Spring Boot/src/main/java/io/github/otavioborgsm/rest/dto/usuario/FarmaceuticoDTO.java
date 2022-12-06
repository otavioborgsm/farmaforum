package io.github.otavioborgsm.rest.dto.usuario;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class FarmaceuticoDTO{

    @NotBlank
    String crf;

    @Size(max = 2)
    @NotBlank
    String estado;
}
