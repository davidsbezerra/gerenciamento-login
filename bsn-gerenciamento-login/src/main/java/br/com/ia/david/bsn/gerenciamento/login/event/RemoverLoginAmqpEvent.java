package br.com.ia.david.bsn.gerenciamento.login.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.ia.david.bsn.gerenciamento.login.annotations.AmqpObject;
import br.com.ia.david.bsn.gerenciamento.login.domain.messaging.Messaging;
import br.com.ia.david.bsn.gerenciamento.login.message.RemoverLoginMessage;
import br.com.ia.david.bsn.gerenciamento.login.response.ErrorResponse;
import br.com.ia.david.bsn.gerenciamento.login.response.RemoverLoginResponse;
import lombok.Data;

@AmqpObject
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public final class RemoverLoginAmqpEvent implements AmqpEvent<RemoverLoginMessage, RemoverLoginResponse> {

    private static final long serialVersionUID = -138472211702550371L;

    private RemoverLoginMessage requisicao;
    private RemoverLoginResponse resultado;
    private ErrorResponse erro;

    @Override
    public String getSuccessRoute() {
        return Messaging.REMOVER_LOGIN_SUCCESS;
    }

    @Override
    public String getErrorRoute() {
        return Messaging.REMOVER_LOGIN_ERROR;
    }
}