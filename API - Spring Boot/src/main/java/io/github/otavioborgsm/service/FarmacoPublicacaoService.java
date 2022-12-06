package io.github.otavioborgsm.service;

import io.github.otavioborgsm.domain.entity.Farmaco;
import io.github.otavioborgsm.domain.entity.FarmacoPublicacao;
import io.github.otavioborgsm.domain.entity.Publicacao;
import io.github.otavioborgsm.rest.dto.farmacoPublicacao.GetFarmacoPublicacaoDTO;

import java.util.List;

public interface FarmacoPublicacaoService {

    FarmacoPublicacao salvar(FarmacoPublicacao farmacoPublicacao);
    void deletar(FarmacoPublicacao farmacoPublicacao);
    GetFarmacoPublicacaoDTO montaDTO(FarmacoPublicacao farmacoPublicacao);
    List<GetFarmacoPublicacaoDTO> montaListaDTO(List<FarmacoPublicacao> farmacoPublicacaoList);
    List<FarmacoPublicacao> salvaLista(List<FarmacoPublicacao> farmacoPublicacaoList);
    List<Publicacao> getPublicacoesPorFarmaco(Farmaco farmaco);
}
