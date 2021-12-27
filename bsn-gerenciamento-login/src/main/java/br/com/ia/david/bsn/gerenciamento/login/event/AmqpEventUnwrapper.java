package br.com.ia.david.bsn.gerenciamento.login.event;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

import java.io.Serializable;

import javax.naming.ServiceUnavailableException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import br.com.ia.david.bsn.gerenciamento.login.domain.ErrorType;
import br.com.ia.david.bsn.gerenciamento.login.domain.Message;
import br.com.ia.david.bsn.gerenciamento.login.exception.AbstractErrorException;
import br.com.ia.david.bsn.gerenciamento.login.exception.ClientErrorException;
import br.com.ia.david.bsn.gerenciamento.login.exception.ServerErrorException;
import br.com.ia.david.bsn.gerenciamento.login.response.ErrorResponse;
import br.com.ia.david.bsn.gerenciamento.login.service.support.MessageService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Service para verificar o resultado de um {@link AmqpEvent} obtido através de request-reply.
 */
@Slf4j
@Component
public class AmqpEventUnwrapper {

    @Autowired
    private MessageService ms;

    /**
     * Identifica se {@link AmqpEvent} contém erros e, se não tiver, retorna objeto de sucesso.
     *
     * @return - Se reply contiver o resultado e não erros, retorna o objeto de sucesso.
     *   <br />- Se reply contiver erro, identifica o status e dispara {@link AbstractErrorException}, a menos que seja
     *     informada uma função para tratamento do erro.
     *   <br />- Se reply for null, dispara {@link ClientErrorException}.
     */
    @SneakyThrows
    public <T extends Serializable, R extends Serializable> R verify(final AmqpEvent<T, R> reply) {

        if (isNull(reply)) {
            throw new ServiceUnavailableException(ms.get(Message.SERVICO_INDISPONIVEL));
        }

        if (nonNull(reply.getErro())) {

            final ErrorResponse error = reply.getErro();
            log.warn("Erro no serviço: {}", error);

            throw identifyErrorException(error);
        }

        return ofNullable(reply.getResultado()).orElseThrow(
            () -> new ClientErrorException(ErrorType.NOT_FOUND, ms.get(Message.NENHUM_RESULTADO_ENCONTRADO)));
    }

    private AbstractErrorException identifyErrorException(final ErrorResponse error) {

        final boolean is4xx = ofNullable(error.getErrorType())
            .map(ErrorType::getHttpStatus)
            .map(HttpStatus::is4xxClientError)
            .orElse(false);

        if (is4xx) {
            return new ClientErrorException(error.getErrorType(), error.getMessage());
        }

        return new ServerErrorException(error.getMessage());
    }

}
