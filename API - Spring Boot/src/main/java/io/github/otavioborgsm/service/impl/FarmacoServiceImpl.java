package io.github.otavioborgsm.service.impl;

import io.github.otavioborgsm.domain.entity.Farmaco;
import io.github.otavioborgsm.domain.repository.FarmacoRepository;
import io.github.otavioborgsm.service.FarmacoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FarmacoServiceImpl implements FarmacoService {

    @Autowired
    FarmacoRepository farmacoRepository;

    public FarmacoServiceImpl(FarmacoRepository farmacoRepository) {
        this.farmacoRepository = farmacoRepository;
    }

    @Override
    @Transactional
    public Farmaco salvar(Farmaco farmaco) {
        return farmacoRepository.save(farmaco);
    }

    @Override
    @Transactional
    public void deletar(Farmaco farmaco) {
        farmacoRepository.delete(farmaco);
    }

    @Override
    public Optional<Farmaco> getById(Long id) {
        return farmacoRepository.findById(id);
    }

    @Override
    public Optional<Farmaco> getByDescricao(String descricao) {
        return farmacoRepository.findTopByDescricao(descricao);
    }

    @Override
    public List<Farmaco> getAll() {
        return farmacoRepository.findAll();
    }

    @Override
    public List<Farmaco> getFarmacosComDescricaoLike(String descricao) {
        return farmacoRepository.findAllByDescricaoLike(descricao);
    }
}
