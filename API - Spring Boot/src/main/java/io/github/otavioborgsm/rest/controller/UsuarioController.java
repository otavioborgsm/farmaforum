package io.github.otavioborgsm.rest.controller;

import io.github.otavioborgsm.domain.entity.Usuario;
import io.github.otavioborgsm.domain.repository.UsuarioRepository;
import io.github.otavioborgsm.exception.SenhaInvalidaException;
import io.github.otavioborgsm.rest.dto.usuario.*;
import io.github.otavioborgsm.security.jwt.JwtService;
import io.github.otavioborgsm.service.impl.UsuarioServiceImpl;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Api("Api de Usuários")
public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("{id}")
    @ApiOperation("Obter detalhes de um usuário.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuário encontardo"),
            @ApiResponse(code = 404, message = "Usuário não encontrado para o id informado")
    })
    public InformacaoUsuarioDTO getUsuarioById(@PathVariable @ApiParam("Id do Usuário") Long id){
        Usuario usuario = usuarioService
                .getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        return usuarioService.montaDTO(usuario);
    }

    @DeleteMapping("{id}")
    @ApiOperation("Deleta um usuário.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @ApiParam("Id do Usuário") Long id){
        usuarioRepository.findById(id)
                .map( usuario -> {
                    usuarioRepository.delete(usuario);
                    return usuario;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Edita usuário.")
    public void update( @PathVariable @ApiParam("Id do usuário.") Long id, @RequestBody EditUsuarioDTO dto){
        usuarioService.editar(id, dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Cria usuário")
    public Usuario salvar(@RequestBody @Valid CreateUsuarioDTO usuarioDTO){
        String senhaCriptografada = passwordEncoder.encode(usuarioDTO.getSenha());
        usuarioDTO.setSenha(senhaCriptografada);
        return usuarioService.criar(usuarioDTO);
    }

    @ApiOperation("Valida o login do usuário e retorna o token de acesso.")
    @PostMapping("/auth")
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais){
        try {
            Usuario usuario = Usuario.builder()
                    .login(credenciais.getLogin())
                    .senha(credenciais.getSenha())
                    .build();
            UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
            Usuario usuarioLogado = usuarioService.getByLogin(credenciais.getLogin());
            String token = jwtService.gerarToken(usuario);

            TokenDTO dto = TokenDTO.builder()
                    .id(usuarioLogado.getId())
                    .login(usuario.getLogin())
                    .token(token)
                    .build();

            if (usuarioLogado.getFarmaceutico() != null){
                dto.setFarmaceutico(usuarioLogado.getFarmaceutico().getId());
            }
            if (usuarioLogado.isAdmin()){
                dto.setIsAdmin(usuarioLogado.isAdmin());
            }
            return dto;
        }catch (UsernameNotFoundException | SenhaInvalidaException  e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
    }
}
