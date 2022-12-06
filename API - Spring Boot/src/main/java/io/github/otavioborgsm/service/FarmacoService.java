package io.github.otavioborgsm.service;

import io.github.otavioborgsm.domain.entity.Farmaco;

import java.util.List;
import java.util.Optional;

public interface FarmacoService {

    Farmaco salvar(Farmaco farmaco);

    void deletar(Farmaco farmaco);

    Optional<Farmaco> getById(Long id);

    Optional<Farmaco> getByDescricao(String descricao);

    List<Farmaco> getAll();

    List<Farmaco> getFarmacosComDescricaoLike(String descricao);
}
