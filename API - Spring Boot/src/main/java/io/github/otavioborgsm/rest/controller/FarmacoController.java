package io.github.otavioborgsm.rest.controller;

import io.github.otavioborgsm.domain.entity.Farmaco;
import io.github.otavioborgsm.domain.repository.FarmacoRepository;
import io.github.otavioborgsm.rest.dto.PesquisaDTO;
import io.github.otavioborgsm.service.impl.FarmacoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/farmacos")
@Api("Api de Farmacos")
public class FarmacoController {

    private FarmacoServiceImpl farmacoService;

    public FarmacoController(FarmacoServiceImpl farmacoService) {
        this.farmacoService = farmacoService;
    }

    @GetMapping("{id}")
    @ApiOperation("Pega Farmaco por Id")
    public Farmaco getFarmacoById(@PathVariable Long id ){
        return farmacoService
                .getById(id)
                .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND , "Farmaco não encontrado" ));

    }

    @PostMapping("/pesquisa")
    @ApiOperation("Pesquisa de Farmaco")
    public List<Farmaco> pesquisaDeFarmaco(@RequestBody @Valid PesquisaDTO dto){
        return farmacoService.getFarmacosComDescricaoLike(dto.getPesquisa());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salva Farmaco")
    public Farmaco save( @RequestBody @Valid Farmaco farmaco ){
        return farmacoService.salvar(farmaco);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deleta Farmaco")
    public void delete( @PathVariable Long id){
        farmacoService.getById(id)
                .map( farmaco -> {
                    farmacoService.deletar(farmaco);
                    return farmaco;
                })
                .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND , "Farmaco não encontrado" ));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Atualiza Farmaco")
    public void update( @PathVariable Long id, @RequestBody @Valid Farmaco farmaco){
        farmacoService
                .getById(id)
                .map( farmacoExistente ->{
                    farmaco.setId(farmacoExistente.getId());
                    farmacoService.salvar(farmaco);
                    return farmacoExistente;
                }).orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND , "Farmaco não encontrado" ));
    }

    @GetMapping
    @ApiOperation("Consulta Farmacos")
    public List<Farmaco> find(){
        return farmacoService.getAll();
    }
}
