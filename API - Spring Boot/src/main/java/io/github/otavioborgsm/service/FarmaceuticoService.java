package io.github.otavioborgsm.service;

import io.github.otavioborgsm.domain.entity.Farmaceutico;
import io.github.otavioborgsm.domain.entity.Usuario;
import io.github.otavioborgsm.rest.dto.usuario.FarmaceuticoDTO;

public interface FarmaceuticoService {
    void salvar(Usuario usuario, FarmaceuticoDTO farmaceuticoDTO);
    void validacaoDeFarmaceutico(Long idUsuario, FarmaceuticoDTO farmaceuticoDTO);
}
