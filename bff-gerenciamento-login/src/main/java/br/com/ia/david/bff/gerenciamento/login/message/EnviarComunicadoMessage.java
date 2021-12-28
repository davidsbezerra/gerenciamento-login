package br.com.ia.david.bff.gerenciamento.login.message;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.ia.david.bff.gerenciamento.login.annotations.AmqpObject;
import lombok.Builder;
import lombok.Getter;

@AmqpObject
@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public final class EnviarComunicadoMessage implements Serializable {

    private static final long serialVersionUID = -7343805635186225719L;
    private final String mensagem;
    private final String login;
}