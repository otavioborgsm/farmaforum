package io.github.otavioborgsm.service;

import io.github.otavioborgsm.domain.entity.Publicacao;
import io.github.otavioborgsm.rest.dto.publicacao.CreatePublicacaoDTO;
import io.github.otavioborgsm.rest.dto.publicacao.EditPublicacaoDTO;
import io.github.otavioborgsm.rest.dto.publicacao.GetPublicacaoDTO;

import java.util.Optional;

public interface PublicacaoService {

    Publicacao criar(CreatePublicacaoDTO dto);
    Optional<Publicacao> getById(Long id);
    GetPublicacaoDTO montaDTO(Publicacao publicacao);
    void deletar(Publicacao publicacao);
    Publicacao editar(Long id, EditPublicacaoDTO dto);

}
