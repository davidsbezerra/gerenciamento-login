package br.com.ia.david.bsn.gerenciamento.login.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.ia.david.bsn.gerenciamento.login.annotations.AmqpObject;
import br.com.ia.david.bsn.gerenciamento.login.domain.messaging.Messaging;
import br.com.ia.david.bsn.gerenciamento.login.message.InserirLoginMessage;
import br.com.ia.david.bsn.gerenciamento.login.response.ErrorResponse;
import br.com.ia.david.bsn.gerenciamento.login.response.InserirLoginResponse;
import lombok.Data;

@AmqpObject
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InserirLoginAmqpEvent implements AmqpEvent<InserirLoginMessage, InserirLoginResponse> {

    private static final long serialVersionUID = -8719862138384339867L;

    private InserirLoginMessage requisicao;
    private InserirLoginResponse resultado;
    private ErrorResponse erro;

    @Override
    public String getSuccessRoute() {
        return Messaging.INSERIR_LOGIN_SUCCESS;
    }

    @Override
    public String getErrorRoute() {
        return Messaging.INSERIR_LOGIN_ERROR;
    }
}