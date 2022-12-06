package io.github.otavioborgsm.service.impl;

import io.github.otavioborgsm.domain.entity.TokenVerificacao;
import io.github.otavioborgsm.domain.repository.TokenVerificacaoRepository;
import io.github.otavioborgsm.exception.RegraNegocioException;
import io.github.otavioborgsm.service.TokenVerificacaoService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

@Service
public class TokenVerificacaoServiceImpl implements TokenVerificacaoService {

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(10);
    private static final Charset UTF = Charset.forName("UTF-8");

    @Value("${security.jwt.expiracao-em-segundos}")
    private int validadeTokenEmSegundos;

    @Autowired
    TokenVerificacaoRepository repository;

    @Override
    public TokenVerificacao criarToken() {
        String valorToken = new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()));
        TokenVerificacao token = new TokenVerificacao();
        token.setToken(valorToken);
        token.setExpiraEm(LocalDateTime.now().plusSeconds(validadeTokenEmSegundos));
        this.salvarToken(token);
        return token;
    }

    @Override
    @Transactional
    public TokenVerificacao salvarToken(TokenVerificacao tokenVerificacao) {
        return repository.save(tokenVerificacao);
    }

    @Override
    public TokenVerificacao findByToken(String token) {
        return repository.findByToken(token)
                .orElseThrow(() -> new RegraNegocioException("Token não encontrado: " + token));
    }

    @Override
    @Transactional
    public void deletaToken(TokenVerificacao tokenVerificacao) {
        repository.delete(tokenVerificacao);
    }

    @Override
    public void deletaTokenPeloToken(String token) {
        repository.deleteByToken(token);
    }

}
