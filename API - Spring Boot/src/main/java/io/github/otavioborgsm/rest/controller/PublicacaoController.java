package io.github.otavioborgsm.rest.controller;

import io.github.otavioborgsm.domain.entity.Publicacao;
import io.github.otavioborgsm.rest.dto.PesquisaDTO;
import io.github.otavioborgsm.rest.dto.publicacao.CreatePublicacaoDTO;
import io.github.otavioborgsm.rest.dto.publicacao.EditPublicacaoDTO;
import io.github.otavioborgsm.rest.dto.publicacao.GetPublicacaoDTO;
import io.github.otavioborgsm.service.impl.PublicacaoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/publicacoes")
@Api("Api de Publicações")
public class PublicacaoController {

    @Autowired
    PublicacaoServiceImpl publicacaoService;

    public PublicacaoController(PublicacaoServiceImpl publicacaoService) {
        this.publicacaoService = publicacaoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salva Publicacao")
    public GetPublicacaoDTO save(@RequestBody @Valid CreatePublicacaoDTO dto){
        Publicacao publicacao = publicacaoService.criar(dto);
        return publicacaoService.montaDTO(publicacao);
    }

    @GetMapping("{id}")
    @ApiOperation("Busca uma publicação pelo seu ID")
    public GetPublicacaoDTO getPublicacaoById(@PathVariable @ApiParam("Id da Publicação.") Long id){
        Publicacao publicacao = publicacaoService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND ,"Publicação não encontrada!"));

        return publicacaoService.montaDTO(publicacao);
    }

    @PostMapping("/pesquisa")
    public List<GetPublicacaoDTO> pesquisa(@RequestBody @Valid PesquisaDTO dto){
        return publicacaoService.pesquisaDePublicacoes(dto.getPesquisa());
    }

    @DeleteMapping("{id}")
    @ApiOperation("Deleta Publicação.")
    public void delete(@PathVariable @ApiParam("Id da Publicação.") Long id){
        Publicacao publicacao = publicacaoService.getById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND ,"Publicação não encontrada!"));
        publicacaoService.deletar(publicacao);
    }

    @PutMapping("{id}")
    @ApiOperation("Edita conteúdo e titulo de uma publicação.")
    public GetPublicacaoDTO editar(
            @PathVariable @ApiParam("Id da publicação.") Long id,
            @RequestBody @Valid EditPublicacaoDTO dto){
        Publicacao publicacao = publicacaoService.editar(id, dto);
        return publicacaoService.montaDTO(publicacao);
    }

}
