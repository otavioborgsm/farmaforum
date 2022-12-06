package io.github.otavioborgsm.rest.controller;

import io.github.otavioborgsm.exception.TokenInvalidoException;
import io.github.otavioborgsm.rest.dto.usuario.FarmaceuticoDTO;
import io.github.otavioborgsm.rest.dto.usuario.recuperarSenha.NovaSenhaDTO;
import io.github.otavioborgsm.rest.dto.usuario.recuperarSenha.RecuperarSenhaDTO;
import io.github.otavioborgsm.service.impl.FarmaceuticoServiceImpl;
import io.github.otavioborgsm.service.impl.UsuarioServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/email")
@Api("Api de E-mails para o usuário")
public class EmailController {

    UsuarioServiceImpl usuarioService;
    PasswordEncoder passwordEncoder;
    FarmaceuticoServiceImpl farmaceuticoService;

    public EmailController(UsuarioServiceImpl usuarioService, PasswordEncoder passwordEncoder, FarmaceuticoServiceImpl farmaceuticoService) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.farmaceuticoService = farmaceuticoService;
    }

    @ApiOperation("Valida o e-mail do usuário.")
    @GetMapping("/verificar")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void verificaTokenValidacao(@RequestParam @ApiParam("Token") String token){
        if(StringUtils.isEmpty(token)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido!");
        }
        usuarioService.verificarUsuario(token);
    }

    @ApiOperation("Envia e-mail para ataulização de senha.")
    @PostMapping("/senha/requisicao")
    public void requisicaoAtualizarSenha(@Valid @RequestBody RecuperarSenhaDTO dto){
        usuarioService.esqueceuASenha(dto.getEmail());
    }

    @ApiOperation("Envia Senha atualizada e Token.")
    @PutMapping("/senha/atualizar")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void atualizarSenha(@Valid @RequestBody NovaSenhaDTO dto) throws TokenInvalidoException {
        NovaSenhaDTO novaSenhaDTO = dto;
        novaSenhaDTO.setSenha(passwordEncoder.encode(novaSenhaDTO.getSenha()));
        usuarioService.alterarSenha(novaSenhaDTO);
    }

    @PostMapping("/validarFarmaceutico/{id}")
    @Secured(value = "USER")
    @ApiOperation("Envia e-mail para solicitar verificação do farmacêutico.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void requisicaoDeAtualizacaoDoUsuario(
            @PathVariable @ApiParam("Id do usuário.") Long id,
            @RequestBody @Valid FarmaceuticoDTO farmaceuticoDTO){
        farmaceuticoService.validacaoDeFarmaceutico(id, farmaceuticoDTO);
    }
}
