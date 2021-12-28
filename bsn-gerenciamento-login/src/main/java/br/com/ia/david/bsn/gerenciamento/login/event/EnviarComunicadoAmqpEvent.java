package br.com.ia.david.bsn.gerenciamento.login.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.ia.david.bsn.gerenciamento.login.annotations.AmqpObject;
import br.com.ia.david.bsn.gerenciamento.login.domain.messaging.Messaging;
import br.com.ia.david.bsn.gerenciamento.login.message.EnviarComunicadoMessage;
import br.com.ia.david.bsn.gerenciamento.login.response.EnviarComunicadoResponse;
import br.com.ia.david.bsn.gerenciamento.login.response.ErrorResponse;
import lombok.Data;

@AmqpObject
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public final class EnviarComunicadoAmqpEvent implements AmqpEvent<EnviarComunicadoMessage, EnviarComunicadoResponse> {

    private static final long serialVersionUID = -138472211702550371L;

    private EnviarComunicadoMessage requisicao;
    private EnviarComunicadoResponse resultado;
    private ErrorResponse erro;

    @Override
    public String getSuccessRoute() {
        return Messaging.ENVIAR_COMUNICADO_SUCCESS;
    }

    @Override
    public String getErrorRoute() {
        return Messaging.ENVIAR_COMUNICADO_ERROR;
    }
}