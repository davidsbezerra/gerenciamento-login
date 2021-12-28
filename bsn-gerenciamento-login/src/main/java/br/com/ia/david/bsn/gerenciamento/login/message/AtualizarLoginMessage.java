package br.com.ia.david.bsn.gerenciamento.login.message;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.ia.david.bsn.gerenciamento.login.annotations.AmqpObject;
import br.com.ia.david.bsn.gerenciamento.login.domain.login.Login;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AmqpObject
@JsonIgnoreProperties(ignoreUnknown = true)
public final class AtualizarLoginMessage implements Serializable {

    private static final long serialVersionUID = 6160174547455856858L;
    private final Login login;
}