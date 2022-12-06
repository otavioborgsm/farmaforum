package io.github.otavioborgsm.service.impl;

import io.github.otavioborgsm.domain.entity.TokenVerificacao;
import io.github.otavioborgsm.domain.entity.Usuario;
import io.github.otavioborgsm.domain.repository.UsuarioRepository;
import io.github.otavioborgsm.exception.SenhaInvalidaException;
import io.github.otavioborgsm.exception.TokenInvalidoException;
import io.github.otavioborgsm.rest.dto.usuario.EditUsuarioDTO;
import io.github.otavioborgsm.rest.dto.usuario.InformacaoUsuarioDTO;
import io.github.otavioborgsm.rest.dto.usuario.InformacoesFarmaceuticoDTO;
import io.github.otavioborgsm.rest.dto.usuario.CreateUsuarioDTO;
import io.github.otavioborgsm.rest.dto.usuario.recuperarSenha.NovaSenhaDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenVerificacaoServiceImpl tokenVerificacaoService;

    @Autowired
    private EmailServiceImpl emailService;

    @Transactional
    public Usuario criar(CreateUsuarioDTO dto){
        Optional<Usuario> usuarioByLogin = repository.findByLogin(dto.getLogin());

        if (usuarioByLogin.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O E-mail informado já está em uso.");
        }else{
            Usuario usuario = new Usuario();
            usuario.setNome(dto.getNome());
            usuario.setSobrenome(dto.getSobrenome());
            usuario.setLogin(dto.getLogin());
            usuario.setSenha(dto.getSenha());
            usuario.setDataCriacao(LocalDate.now());

            Usuario usuarioSalvo = repository.save(usuario);
            this.enviaEmailValidacao(usuarioSalvo);
            return usuarioSalvo;
        }
    }

    public Optional<Usuario> getById(Long id) {
        return repository.findById(id);
    }

    public List<Usuario> getAllUsuarios(){
        return repository.findAll();
    }

    @Transactional
    public void editar(Long id, EditUsuarioDTO dto){
        repository.findById(id)
                .map(usuario -> {
                    usuario.setNome(dto.getNome());
                    usuario.setSobrenome(dto.getSobrenome());
                    return repository.save(usuario);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    public InformacaoUsuarioDTO montaDTO(Usuario usuario){

        InformacaoUsuarioDTO dto = new InformacaoUsuarioDTO();
        if (usuario.getFarmaceutico() != null){
            dto = new InformacoesFarmaceuticoDTO();
        }

        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setSobrenome(usuario.getSobrenome());
        dto.setDataCriacao(usuario.getDataCriacao());

        if (dto instanceof InformacoesFarmaceuticoDTO){
            ((InformacoesFarmaceuticoDTO) dto).setCrf(usuario.getFarmaceutico().getCrf());
            ((InformacoesFarmaceuticoDTO) dto).setEstado(usuario.getFarmaceutico().getEstado());
        }

        return dto;
    }

    public UserDetails autenticar(Usuario usuario){
        UserDetails user = loadUserByUsername(usuario.getLogin());
        boolean senhasBatem = encoder.matches(usuario.getSenha(), user.getPassword());
        if (senhasBatem){
            return user;
        }
        throw new SenhaInvalidaException();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repository
                .findByLogin(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

//        Boolean ativo = !usuario.isContaVerificada();
        String[] roles = usuario.isAdmin() ? new String[]{"ADMIN", "USER"} : new String[]{"USER"};

        if (usuario.getFarmaceutico() != null){
            roles = new String[]{"FARMACEUTICO", "USER"};
            if (usuario.isAdmin()){
                roles = new String[]{"ADMIN", "USER", "FARMACEUTICO"};
            }
        }

        System.out.println(roles);
        return User
                .builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(roles)
//                .disabled(ativo)
                .build();
    }

    public Usuario salvar(Usuario usuario){
        return repository.save(usuario);
    }

    public Usuario getByLogin(String login) throws UsernameNotFoundException{
        return repository.findByLogin(login)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    public boolean verificarUsuario(String token){
        TokenVerificacao tokenVerificacao = tokenVerificacaoService.findByToken(token);
        if(Objects.isNull(tokenVerificacao) || !StringUtils.equals(token, tokenVerificacao.getToken()) || tokenVerificacao.isExpirado()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token informado incorretamente!");
        }
        Usuario usuario = this.getById(tokenVerificacao.getUsuario().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        if(Objects.isNull(usuario)){
            return false;
        }
        usuario.setContaVerificada(true);
        repository.save(usuario);

        tokenVerificacaoService.deletaToken(tokenVerificacao);

        return true;
    }

    protected void enviaEmailValidacao(Usuario usuario){
        TokenVerificacao token = tokenVerificacaoService.criarToken();
        token.setUsuario(usuario);
        TokenVerificacao tokenVerificacao = tokenVerificacaoService.salvarToken(token);

        try {
            emailService.enviarEmail(
                    usuario.getLogin(),
                    "Token para verificação de e-mail FarmaForum.",
                    "Seu token de verificação é:\n\n" + tokenVerificacao.getToken()
            );
        } catch (MessagingException e){
            e.printStackTrace();
        }
    }

    public void esqueceuASenha(String login) throws UsernameNotFoundException{
        Usuario usuario = this.getByLogin(login);
        this.enviaEmailAtualizarSenha(usuario);
    }

    public void alterarSenha(NovaSenhaDTO dto){
        TokenVerificacao tokenVerificacao = tokenVerificacaoService.findByToken(dto.getToken());
        if(Objects.isNull(tokenVerificacao) || !StringUtils.equals(dto.getToken(), tokenVerificacao.getToken()) || tokenVerificacao.isExpirado()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token informado não encontrado!");
        }
        Usuario usuario = repository.findById(tokenVerificacao.getUsuario().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        tokenVerificacaoService.deletaToken(tokenVerificacao);
        usuario.setSenha(dto.getSenha());
        salvar(usuario);
    }

    protected void enviaEmailAtualizarSenha(Usuario usuario){
        TokenVerificacao token = tokenVerificacaoService.criarToken();
        token.setUsuario(usuario);
        TokenVerificacao tokenVerificacao = tokenVerificacaoService.salvarToken(token);

        try {
            emailService.enviarEmail(
                    usuario.getLogin(),
                    "Token para atualização de senha FarmaForum.",
                    "Seu token para atualizar a senha é:\n\n" + tokenVerificacao.getToken()
            );
        } catch (MessagingException e){
            e.printStackTrace();
        }
    }
}
