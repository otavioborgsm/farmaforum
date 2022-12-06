package io.github.otavioborgsm.domain.repository;

import io.github.otavioborgsm.domain.entity.Publicacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicacaoRepository extends JpaRepository<Publicacao, Long> {
    List<Publicacao> findAllByTituloLikeOrConteudoLike(String pesquisaTitulo, String pesquisaConteudo);
    List<Publicacao> getAllByTituloLike(String titulo);
    List<Publicacao> getAllByConteudoLike(String conteudo);

}
