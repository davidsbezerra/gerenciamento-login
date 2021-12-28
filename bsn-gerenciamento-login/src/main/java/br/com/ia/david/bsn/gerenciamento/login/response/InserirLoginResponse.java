package br.com.ia.david.bsn.gerenciamento.login.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.ia.david.bsn.gerenciamento.login.domain.login.Login;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InserirLoginResponse implements Serializable {

    private static final long serialVersionUID = 2803384983817151516L;

    private Login login;

}