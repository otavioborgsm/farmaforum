package io.github.otavioborgsm.domain.repository;

import io.github.otavioborgsm.domain.entity.Farmaco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FarmacoRepository extends JpaRepository<Farmaco, Long> {

    Optional<Farmaco> findTopByDescricao(String descricao);
    List<Farmaco> findAllByDescricaoLike(String descricao);
}
