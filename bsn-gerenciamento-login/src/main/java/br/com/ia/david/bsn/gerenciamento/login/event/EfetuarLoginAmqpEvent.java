package br.com.ia.david.bsn.gerenciamento.login.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.ia.david.bsn.gerenciamento.login.annotations.AmqpObject;
import br.com.ia.david.bsn.gerenciamento.login.domain.messaging.Messaging;
import br.com.ia.david.bsn.gerenciamento.login.message.EfetuarLoginMessage;
import br.com.ia.david.bsn.gerenciamento.login.response.EfetuarLoginResponse;
import br.com.ia.david.bsn.gerenciamento.login.response.ErrorResponse;
import lombok.Data;

@AmqpObject
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public final class EfetuarLoginAmqpEvent implements AmqpEvent<EfetuarLoginMessage, EfetuarLoginResponse> {

    private static final long serialVersionUID = -4470675259956221484L;

    private EfetuarLoginMessage requisicao;
    private EfetuarLoginResponse resultado;
    private ErrorResponse erro;

    @Override
    public String getSuccessRoute() {
        return Messaging.EFETUAR_LOGIN_SUCCESS;
    }

    @Override
    public String getErrorRoute() {
        return Messaging.EFETUAR_LOGIN_ERROR;
    }
}