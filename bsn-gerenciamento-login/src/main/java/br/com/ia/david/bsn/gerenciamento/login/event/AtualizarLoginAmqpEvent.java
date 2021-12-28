package br.com.ia.david.bsn.gerenciamento.login.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.ia.david.bsn.gerenciamento.login.annotations.AmqpObject;
import br.com.ia.david.bsn.gerenciamento.login.domain.messaging.Messaging;
import br.com.ia.david.bsn.gerenciamento.login.message.AtualizarLoginMessage;
import br.com.ia.david.bsn.gerenciamento.login.response.AtualizarLoginResponse;
import br.com.ia.david.bsn.gerenciamento.login.response.ErrorResponse;
import lombok.Data;

@AmqpObject
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public final class AtualizarLoginAmqpEvent implements AmqpEvent<AtualizarLoginMessage, AtualizarLoginResponse> {

    private static final long serialVersionUID = 6375707485008593733L;

    private AtualizarLoginMessage requisicao;
    private AtualizarLoginResponse resultado;
    private ErrorResponse erro;

    @Override
    public String getSuccessRoute() {
        return Messaging.ATUALIZAR_LOGIN_SUCCESS;
    }

    @Override
    public String getErrorRoute() {
        return Messaging.ATUALIZAR_LOGIN_ERROR;
    }
}