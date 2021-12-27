package br.com.ia.david.bsn.gerenciamento.login.event;

import br.com.ia.david.bsn.gerenciamento.login.response.ErrorResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter @Builder
public class AmqpEventImpl implements AmqpEvent<AmqpEventImpl.RequisicaoImpl, AmqpEventImpl.ResponseImpl> {

    private ResponseImpl resultado;
    private RequisicaoImpl requisicao;
    private ErrorResponse erro;

    @Override
    public String getSuccessRoute() {
        return null;
    }

    @Override
    public String getErrorRoute() {
        return null;
    }

    public static class ResponseImpl implements Serializable { }

    public static class RequisicaoImpl implements Serializable { }

}
