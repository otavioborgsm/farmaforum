package io.github.otavioborgsm.rest.controller;

import io.github.otavioborgsm.domain.entity.Comentario;
import io.github.otavioborgsm.rest.dto.comentario.CreateComentarioDTO;
import io.github.otavioborgsm.rest.dto.comentario.EditComentarioDTO;
import io.github.otavioborgsm.rest.dto.comentario.GetComentarioDTO;
import io.github.otavioborgsm.service.impl.ComentarioServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/comentarios")
@Api("Api de Comentário")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioServiceImpl service;

    @PostMapping
    @ApiOperation("Cria comentário")
    public GetComentarioDTO create(@RequestBody CreateComentarioDTO dto){
        Comentario comentario = service.criar(dto);
        return service.montaDTO(comentario);
    }

    @DeleteMapping("{id}")
    @ApiOperation("Deleta Comentário")
    public void delete(@PathVariable Long id){
        Comentario comentario = service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmaco não encontrado"));
        service.deletar(comentario);
    }

    @PutMapping("{id}")
    @ApiOperation("Edita conteúdo de um comentário")
    public GetComentarioDTO editar(
            @PathVariable @ApiParam("Id do Comentário") Long id,
            @RequestBody @Valid EditComentarioDTO dto){
        Comentario comentario = service.editar(id, dto);
        return service.montaDTO(comentario);
    }

}
