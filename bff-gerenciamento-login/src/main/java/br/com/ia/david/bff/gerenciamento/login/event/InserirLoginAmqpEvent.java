package br.com.ia.david.bff.gerenciamento.login.event;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.ia.david.bff.gerenciamento.login.annotations.AmqpObject;
import br.com.ia.david.bff.gerenciamento.login.domain.login.Login;
import br.com.ia.david.bff.gerenciamento.login.event.InserirLoginAmqpEvent.InserirLoginResponse;
import br.com.ia.david.bff.gerenciamento.login.message.InserirLoginMessage;
import br.com.ia.david.bff.gerenciamento.login.response.ErrorResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AmqpObject
@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InserirLoginAmqpEvent implements AmqpEvent<InserirLoginMessage, InserirLoginResponse> {

    private static final long serialVersionUID = 6139617789495530368L;

    private InserirLoginResponse resultado;
    private ErrorResponse erro;
    private InserirLoginMessage requisicao;

    @Override
    public String getSuccessRoute() {
        return null;
    }

    @Override
    public String getErrorRoute() {
        return null;
    }


    @Getter
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InserirLoginResponse implements Serializable {

        private static final long serialVersionUID = -5816467503264784382L;

        private final Login login;
    }
}