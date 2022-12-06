package io.github.otavioborgsm.rest.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InformacaoUsuarioDTO {
    private Long id;
    private String nome;
    private String sobrenome;
    private LocalDate dataCriacao;
}
