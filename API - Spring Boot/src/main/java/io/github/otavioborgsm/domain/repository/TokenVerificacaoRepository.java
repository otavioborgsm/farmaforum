package io.github.otavioborgsm.domain.repository;

import io.github.otavioborgsm.domain.entity.TokenVerificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenVerificacaoRepository extends JpaRepository<TokenVerificacao, Long> {
    Optional<TokenVerificacao> findByToken(final String token);
    Long removeByToken(String token);
    void deleteByToken(String token);
}
