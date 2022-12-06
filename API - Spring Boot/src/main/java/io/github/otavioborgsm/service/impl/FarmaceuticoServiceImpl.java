package io.github.otavioborgsm.service.impl;

import io.github.otavioborgsm.domain.entity.Farmaceutico;
import io.github.otavioborgsm.domain.entity.Usuario;
import io.github.otavioborgsm.domain.enums.Estado;
import io.github.otavioborgsm.domain.repository.FarmaceuticoRepository;
import io.github.otavioborgsm.rest.dto.usuario.FarmaceuticoDTO;
import io.github.otavioborgsm.service.FarmaceuticoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;

@Service
public class FarmaceuticoServiceImpl implements FarmaceuticoService {

    FarmaceuticoRepository repository;
    UsuarioServiceImpl usuarioService;
    EmailServiceImpl emailService;

    public FarmaceuticoServiceImpl(FarmaceuticoRepository repository, UsuarioServiceImpl usuarioService, EmailServiceImpl emailService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public void salvar(Usuario usuario, FarmaceuticoDTO farmaceuticoDTO) {
        Farmaceutico farmaceutico = new Farmaceutico();
        farmaceutico.setCrf(farmaceuticoDTO.getCrf());
        farmaceutico.setEstado(Estado.valueOf(farmaceuticoDTO.getEstado()));
        farmaceutico.setUsuario(usuario);
        Farmaceutico farmaceuticoSalvo = repository.save(farmaceutico);
        usuario.setFarmaceutico(farmaceuticoSalvo);
        usuarioService.salvar(usuario);
    }

    @Override
    public void validacaoDeFarmaceutico(Long idUsuario, FarmaceuticoDTO farmaceuticoDTO) {

        try {
            emailService.enviarEmail(
                    "farmaforum.validacao@gmail.com",
                    "O Usuário id: " + idUsuario + " Deseja se tornar um farmacêutico.",
                    "CRF informado: " + farmaceuticoDTO.getCrf() +
                          "\nEstado informado: " + farmaceuticoDTO.getEstado()
            );
        } catch (MessagingException e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ocorreu um erro na validação de farmacêutico");
        }

    }
}
