package io.github.otavioborgsm.domain.repository;

import io.github.otavioborgsm.domain.entity.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
}
