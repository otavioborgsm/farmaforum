package io.github.otavioborgsm.service;

import io.github.otavioborgsm.domain.entity.TokenVerificacao;

public interface TokenVerificacaoService {

    TokenVerificacao criarToken();

    TokenVerificacao salvarToken(TokenVerificacao tokenVerificacao);

    TokenVerificacao findByToken(String token);

    void deletaToken(TokenVerificacao tokenVerificacao);

    void deletaTokenPeloToken(String token);

}
