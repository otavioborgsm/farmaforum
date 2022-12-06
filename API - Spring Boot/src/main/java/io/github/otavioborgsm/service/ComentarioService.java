package io.github.otavioborgsm.service;

import io.github.otavioborgsm.domain.entity.Comentario;
import io.github.otavioborgsm.rest.dto.comentario.CreateComentarioDTO;
import io.github.otavioborgsm.rest.dto.comentario.EditComentarioDTO;
import io.github.otavioborgsm.rest.dto.comentario.GetComentarioDTO;

import java.util.List;
import java.util.Optional;

public interface ComentarioService {

    Comentario criar(CreateComentarioDTO dto);

    void deletar(Comentario comentario);

    Comentario editar(Long id, EditComentarioDTO dto);

    Optional<Comentario> getById(Long id);

    GetComentarioDTO montaDTO(Comentario comentario);

    List<GetComentarioDTO> montaListaDTO(List<Comentario> comentarios);

    Comentario refutarComentario(Long id);

}
