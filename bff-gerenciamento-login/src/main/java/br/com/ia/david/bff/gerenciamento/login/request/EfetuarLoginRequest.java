package br.com.ia.david.bff.gerenciamento.login.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EfetuarLoginRequest implements Serializable {

    private static final long serialVersionUID = -1286872387442482566L;

    private final String login;
    private final String password;

}
