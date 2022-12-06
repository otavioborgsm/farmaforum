package io.github.otavioborgsm.domain.repository;

import io.github.otavioborgsm.domain.entity.Farmaco;
import io.github.otavioborgsm.domain.entity.FarmacoPublicacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FarmacoPublicacaoRepository extends JpaRepository<FarmacoPublicacao, Long> {
    List<FarmacoPublicacao> findAllByFarmaco(Farmaco farmaco);
}
