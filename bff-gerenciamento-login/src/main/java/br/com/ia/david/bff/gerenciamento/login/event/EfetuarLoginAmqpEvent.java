package br.com.ia.david.bff.gerenciamento.login.event;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.ia.david.bff.gerenciamento.login.annotations.AmqpObject;
import br.com.ia.david.bff.gerenciamento.login.domain.login.Login;
import br.com.ia.david.bff.gerenciamento.login.event.EfetuarLoginAmqpEvent.EfetuarLoginResponse;
import br.com.ia.david.bff.gerenciamento.login.message.EfetuarLoginMessage;
import br.com.ia.david.bff.gerenciamento.login.response.ErrorResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AmqpObject
@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public final class EfetuarLoginAmqpEvent implements AmqpEvent<EfetuarLoginMessage, EfetuarLoginResponse> {

    private static final long serialVersionUID = -5826299698351475936L;

    private EfetuarLoginResponse resultado;
    private ErrorResponse erro;
    private EfetuarLoginMessage requisicao;

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
    public static class EfetuarLoginResponse implements Serializable {

        private static final long serialVersionUID = 2076695019941459291L;

        private final Login login;
    }
}