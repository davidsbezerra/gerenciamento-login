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
public final class RemoverLoginMessage implements Serializable {

    private static final long serialVersionUID = -1581637191891639179L;
    private final Long id;
}