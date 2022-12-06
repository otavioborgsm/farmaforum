package io.github.otavioborgsm.service.impl;

import io.github.otavioborgsm.domain.entity.Farmaco;
import io.github.otavioborgsm.domain.entity.FarmacoPublicacao;
import io.github.otavioborgsm.domain.entity.Publicacao;
import io.github.otavioborgsm.domain.entity.Usuario;
import io.github.otavioborgsm.domain.repository.PublicacaoRepository;
import io.github.otavioborgsm.rest.dto.farmacoPublicacao.GetFarmacoPublicacaoDTO;
import io.github.otavioborgsm.rest.dto.publicacao.CreatePublicacaoDTO;
import io.github.otavioborgsm.rest.dto.publicacao.EditPublicacaoDTO;
import io.github.otavioborgsm.rest.dto.publicacao.GetPublicacaoDTO;
import io.github.otavioborgsm.service.PublicacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PublicacaoServiceImpl implements PublicacaoService {

    private PublicacaoRepository repository;
    private FarmacoPublicacaoServiceImpl farmacoPublicacaoService;
    private FarmacoServiceImpl farmacoService;
    private ComentarioServiceImpl comentarioService;
    private UsuarioServiceImpl usuarioService;

    public PublicacaoServiceImpl(PublicacaoRepository repository, FarmacoPublicacaoServiceImpl farmacoPublicacaoService, FarmacoServiceImpl farmacoService, ComentarioServiceImpl comentarioService, UsuarioServiceImpl usuarioService) {
        this.repository = repository;
        this.farmacoPublicacaoService = farmacoPublicacaoService;
        this.farmacoService = farmacoService;
        this.comentarioService = comentarioService;
        this.usuarioService = usuarioService;
    }

    @Override
    @Transactional
    public Publicacao criar(CreatePublicacaoDTO dto) {
        Long idUsuario = dto.getIdUsuario();
        Usuario usuario = usuarioService.getById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        Publicacao publicacao = new Publicacao();
        publicacao.setTitulo(dto.getTitulo());
        publicacao.setConteudo(dto.getConteudo());
        publicacao.setDataCriacao(LocalDateTime.now());
        publicacao.setUsuario(usuario);

        if (dto.getFarmacos() != null){
            List<FarmacoPublicacao> farmacos = converterFarmacos(publicacao, dto.getFarmacos());
            repository.save(publicacao);
            farmacoPublicacaoService.salvaLista(farmacos);
            publicacao.setFarmacoPublicacaoList(farmacos);
        }else{
            repository.save(publicacao);
        }

        return publicacao;
    }

    public List<GetPublicacaoDTO> pesquisaDePublicacoes(String pesquisa){
        String pesquisaLike = "%" + pesquisa + "%";
        List<Publicacao> resultado = new ArrayList<>();
        resultado.addAll(repository.findAllByTituloLikeOrConteudoLike(pesquisaLike, pesquisaLike));

        List<Farmaco> listaFarmacosPesquisa = farmacoService.getFarmacosComDescricaoLike(pesquisaLike);
        List<List<Publicacao>> publicacoesFarmaco = listaFarmacosPesquisa.stream()
                .map(farmaco -> farmacoPublicacaoService.getPublicacoesPorFarmaco(farmaco))
                .collect(Collectors.toList());

        List<Publicacao> resultadoPesquisaFarmaco = new ArrayList<>();
        publicacoesFarmaco.forEach(publicacaos -> resultadoPesquisaFarmaco.addAll(publicacaos));

        resultado.addAll(resultadoPesquisaFarmaco);
        while (resultado.remove(null)) {
        }

        return resultado.stream().map(this::montaDTO).collect(Collectors.toList());
    }

    private List<FarmacoPublicacao> converterFarmacos(Publicacao publicacao, List<GetFarmacoPublicacaoDTO> farmacos) {
        return farmacos
                .stream()
                .map(dto ->{
                    Farmaco farmaco = farmacoService
                            .getByDescricao(dto.getFarmaco().getDescricao())
                            .orElse(farmacoService.salvar(new Farmaco(dto.getFarmaco().getDescricao())));

                    FarmacoPublicacao farmacoPublicacao = new FarmacoPublicacao();
                    farmacoPublicacao.setFarmaco(farmaco);
                    farmacoPublicacao.setPublicacao(publicacao);
                    return farmacoPublicacao;
                }).collect(Collectors.toList());
    }

    @Override
    public Optional<Publicacao> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public GetPublicacaoDTO montaDTO(Publicacao publicacao){
        GetPublicacaoDTO dto = new GetPublicacaoDTO();
        dto.setId(publicacao.getId());
        dto.setTitulo(publicacao.getTitulo());
        dto.setConteudo(publicacao.getConteudo());
        dto.setDataCriacao(publicacao.getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        if (publicacao.getFarmacoPublicacaoList() != null){
            dto.setFarmacos(farmacoPublicacaoService.montaListaDTO(publicacao.getFarmacoPublicacaoList()));
        }
        if (publicacao.getComentarios() != null){
            dto.setComentarios(comentarioService.montaListaDTO(publicacao.getComentarios()));
        }
        dto.setUsuario(usuarioService.montaDTO(publicacao.getUsuario()));
        return dto;
    }

    @Override
    @Transactional
    public void deletar(Publicacao publicacao) {
        repository.delete(publicacao);
    }

    @Override
    @Transactional
    public Publicacao editar(Long id, EditPublicacaoDTO dto) {
        Publicacao publicacao = getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publicação não encontrada!"));
        publicacao.setTitulo(dto.getTitulo());
        publicacao.setConteudo(dto.getConteudo());
        return repository.save(publicacao);
    }
}
