package io.github.otavioborgsm.rest.controller;

import io.github.otavioborgsm.domain.entity.Usuario;
import io.github.otavioborgsm.domain.repository.UsuarioRepository;
import io.github.otavioborgsm.rest.dto.usuario.FarmaceuticoDTO;
import io.github.otavioborgsm.service.impl.ComentarioServiceImpl;
import io.github.otavioborgsm.service.impl.FarmaceuticoServiceImpl;
import io.github.otavioborgsm.service.impl.UsuarioServiceImpl;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/farmaceuticos")
@Api("Api de Farmaceuticos")
public class FarmaceuticoController {

    FarmaceuticoServiceImpl farmaceuticoService;
    ComentarioServiceImpl comentarioService;
    UsuarioServiceImpl usuarioService;

    public FarmaceuticoController(FarmaceuticoServiceImpl farmaceuticoService, ComentarioServiceImpl comentarioService, UsuarioServiceImpl usuarioService) {
        this.farmaceuticoService = farmaceuticoService;
        this.comentarioService = comentarioService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Atualiza usuário para farmacêutico.")
    public void criaFarmaceutico(
            @PathVariable @ApiParam("Id do usuário.") Long id,
            @RequestBody @Valid FarmaceuticoDTO farmaceuticoDTO)
    {
        Usuario usuario = usuarioService
                .getById(id)
                .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND , "Usuário não encontrado!" ));
        farmaceuticoService.salvar(usuario, farmaceuticoDTO);
    }

    @PutMapping("/refutarComentario/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Refuta Comentário")
    public void refutarComentario(@PathVariable @ApiParam("Id do comentário.") Long id){
        comentarioService.refutarComentario(id);
    }


}
