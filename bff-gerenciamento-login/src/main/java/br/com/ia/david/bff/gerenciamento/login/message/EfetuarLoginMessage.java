package br.com.ia.david.bff.gerenciamento.login.message;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.ia.david.bff.gerenciamento.login.annotations.AmqpObject;
import br.com.ia.david.bff.gerenciamento.login.domain.login.Login;
import lombok.Builder;
import lombok.Getter;

@AmqpObject
@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public final class EfetuarLoginMessage implements Serializable {

    private static final long serialVersionUID = 5556469903094717163L;
    private final Login login;
}