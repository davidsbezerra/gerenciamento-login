package br.com.ia.david.bsn.gerenciamento.login.event;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.ia.david.bsn.gerenciamento.login.response.ErrorResponse;

/**
 * Contrato para definição de eventos de resposta assíncrona e objetos de retorno para chamadas request-reply.
 *
 * @param <T> Representa o tipo do objeto de entrada (requisição).
 * @param <R> Representa o tipo do objeto de saída (sucesso).
 */
public interface AmqpEvent<T extends Serializable, R extends Serializable> extends Serializable {

    T getRequisicao();
    void setRequisicao(T requisicao);

    ErrorResponse getErro();
    void setErro(ErrorResponse erro);

    R getResultado();
    void setResultado(R resultado);

    /**
     * Retorna a rota à qual será enviado o evento de sucesso em caso de uma chamada assíncrona.
     */
    @JsonIgnore
    String getSuccessRoute();

    /**
     * Retorna a rota à qual será enviado o evento de erro em caso de uma chamada assíncrona.
     */
    @JsonIgnore
    String getErrorRoute();
}

