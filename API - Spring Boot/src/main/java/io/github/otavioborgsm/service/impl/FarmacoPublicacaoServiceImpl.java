package io.github.otavioborgsm.service.impl;

import io.github.otavioborgsm.domain.entity.Farmaco;
import io.github.otavioborgsm.domain.entity.FarmacoPublicacao;
import io.github.otavioborgsm.domain.entity.Publicacao;
import io.github.otavioborgsm.domain.repository.FarmacoPublicacaoRepository;
import io.github.otavioborgsm.rest.dto.farmacoPublicacao.GetFarmacoPublicacaoDTO;
import io.github.otavioborgsm.service.FarmacoPublicacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FarmacoPublicacaoServiceImpl implements FarmacoPublicacaoService {

    @Autowired
    FarmacoPublicacaoRepository repository;

    @Override
    @Transactional
    public FarmacoPublicacao salvar(FarmacoPublicacao farmacoPublicacao) {
        return repository.save(farmacoPublicacao);
    }

    @Override
    @Transactional
    public void deletar(FarmacoPublicacao farmacoPublicacao) {
        repository.delete(farmacoPublicacao);
    }

    @Override
    public GetFarmacoPublicacaoDTO montaDTO(FarmacoPublicacao farmacoPublicacao) {
        return GetFarmacoPublicacaoDTO.builder()
                .id(farmacoPublicacao.getId())
                .idPublicacao(farmacoPublicacao.getPublicacao().getId())
                .farmaco(farmacoPublicacao.getFarmaco())
                .build();
    }

    @Override
    public List<GetFarmacoPublicacaoDTO> montaListaDTO(List<FarmacoPublicacao> farmacoPublicacaoList) {
        return farmacoPublicacaoList.stream().map(this::montaDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<FarmacoPublicacao> salvaLista(List<FarmacoPublicacao> farmacoPublicacaoList) {
        return repository.saveAll(farmacoPublicacaoList);
    }

    @Override
    public List<Publicacao> getPublicacoesPorFarmaco(Farmaco farmaco) {
        List<Publicacao> resultado = new ArrayList<>();
        List<FarmacoPublicacao> listaFarmacoPublicacao = repository.findAllByFarmaco(farmaco);

        resultado.addAll(listaFarmacoPublicacao.stream().map(lista -> lista.getPublicacao()).collect(Collectors.toList()));

        return resultado;
    }
}
