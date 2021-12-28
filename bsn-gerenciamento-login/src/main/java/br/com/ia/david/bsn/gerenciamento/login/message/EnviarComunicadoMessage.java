package br.com.ia.david.bsn.gerenciamento.login.message;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.ia.david.bsn.gerenciamento.login.annotations.AmqpObject;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AmqpObject
@JsonIgnoreProperties(ignoreUnknown = true)
public final class EnviarComunicadoMessage implements Serializable {

    private static final long serialVersionUID = -233045297692193663L;
    private final String mensagem;
    private final String login;
}