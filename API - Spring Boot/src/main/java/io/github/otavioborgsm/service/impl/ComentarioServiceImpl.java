package io.github.otavioborgsm.service.impl;

import io.github.otavioborgsm.domain.entity.Comentario;
import io.github.otavioborgsm.domain.repository.ComentarioRepository;
import io.github.otavioborgsm.rest.dto.comentario.CreateComentarioDTO;
import io.github.otavioborgsm.rest.dto.comentario.EditComentarioDTO;
import io.github.otavioborgsm.rest.dto.comentario.GetComentarioDTO;
import io.github.otavioborgsm.service.ComentarioService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComentarioServiceImpl implements ComentarioService {

    private ComentarioRepository repository;
    private UsuarioServiceImpl usuarioService;

    public ComentarioServiceImpl(ComentarioRepository repository, UsuarioServiceImpl usuarioService, @Lazy PublicacaoServiceImpl publicacaoService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
        this.publicacaoService = publicacaoService;
    }

    private PublicacaoServiceImpl publicacaoService;

    @Override
    @Transactional
    public Comentario criar(CreateComentarioDTO dto) {
        Comentario comentario = new Comentario();
        comentario.setConteudo(dto.getConteudo());
        comentario.setRefutado(false);
        comentario.setDataCriacao(LocalDateTime.now());
        comentario.setUsuario(usuarioService.getById(dto.getIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Uusário não encontrado!")));
        comentario.setPublicacao(publicacaoService.getById(dto.getIdPublicacao())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publicação não foi encontrada!")));
        return repository.save(comentario);
    }

    @Override
    @Transactional
    public void deletar(Comentario comentario) {
        repository.delete(comentario);
    }

    @Override
    @Transactional
    public Comentario editar(Long id, EditComentarioDTO dto) {
        Comentario comentario = getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comentario não encontrado"));
        comentario.setConteudo(dto.getConteudo());
        return repository.save(comentario);
    }

    @Override
    public Optional<Comentario> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public GetComentarioDTO montaDTO(Comentario comentario) {
        return GetComentarioDTO.builder()
                .id(comentario.getId())
                .conteudo(comentario.getConteudo())
                .refutado(comentario.getRefutado())
                .dataCriacao(comentario.getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .publicacaoId(comentario.getPublicacao().getId())
                .usuario(usuarioService.montaDTO(comentario.getUsuario()))
                .build();
    }

    @Override
    public List<GetComentarioDTO> montaListaDTO(List<Comentario> comentarios) {
        return comentarios.stream().map(this::montaDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Comentario refutarComentario(Long id) {
        Comentario comentario = getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comentario não encontrado!"));
        comentario.setRefutado(!comentario.getRefutado());
        return repository.save(comentario);
    }
}
