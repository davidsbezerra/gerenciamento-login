package br.com.ia.david.bsn.gerenciamento.login.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public final class RemoverLoginResponse implements Serializable {

    private static final long serialVersionUID = 803384983817151516L;

    private boolean sucesso;

}