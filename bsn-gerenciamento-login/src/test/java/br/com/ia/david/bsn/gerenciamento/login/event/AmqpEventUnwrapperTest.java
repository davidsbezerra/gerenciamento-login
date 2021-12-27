package br.com.ia.david.bsn.gerenciamento.login.event;

import static br.com.ia.david.bsn.gerenciamento.login.domain.ErrorType.VALIDATION;
import static br.com.ia.david.bsn.gerenciamento.login.domain.Message.NENHUM_RESULTADO_ENCONTRADO;
import static br.com.ia.david.bsn.gerenciamento.login.domain.Message.SERVICO_INDISPONIVEL;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import javax.naming.ServiceUnavailableException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.ia.david.bsn.gerenciamento.login.exception.ClientErrorException;
import br.com.ia.david.bsn.gerenciamento.login.exception.ServerErrorException;
import br.com.ia.david.bsn.gerenciamento.login.response.ErrorResponse;
import br.com.ia.david.bsn.gerenciamento.login.service.support.MessageService;

@RunWith(MockitoJUnitRunner.class)
public class AmqpEventUnwrapperTest {

    @InjectMocks
    private AmqpEventUnwrapper amqpEventUnwrapper;

    @Mock
    private MessageService messageService;

    @Test
    public void deveRetornarObjetoDeResultadoDoEvento() {
        final AmqpEventImpl.ResponseImpl resultadoEsperado = new AmqpEventImpl.ResponseImpl();
        final AmqpEventImpl event = AmqpEventImpl.builder().resultado(resultadoEsperado).build();
        final AmqpEventImpl.ResponseImpl resultadoAtual = amqpEventUnwrapper.verify(event);
        assertEquals(resultadoEsperado, resultadoAtual);
    }

    @Test(expected = ServiceUnavailableException.class)
    public void deveRetornarServiceUnavailableExceptionQuandoEventoNulo() {
        try {
            amqpEventUnwrapper.verify(null);
        } finally {
            verify(messageService).get(eq(SERVICO_INDISPONIVEL));
        }
    }

    @Test(expected = ClientErrorException.class)
    public void deveRetornarClientErrorExceptionQuandoResultadoDoEventoForNulo() {
        final AmqpEventImpl event = AmqpEventImpl.builder().build();
        try {
            amqpEventUnwrapper.verify(event);
        } finally {
            verify(messageService).get(eq(NENHUM_RESULTADO_ENCONTRADO));
        }
    }

    @Test(expected = ClientErrorException.class)
    public void deveRetornarClientErrorExceptionQuandoExistirObjetoDeErroNoEvento() {
        final AmqpEventImpl event = AmqpEventImpl.builder().erro(
            ErrorResponse.builder().errorType(VALIDATION).build()).build();
        amqpEventUnwrapper.verify(event);
    }

    @Test(expected = ServerErrorException.class)
    public void deveRetornarServerErrorExceptionQuandoErrorTypeNuloDoEvento() {
        final AmqpEventImpl event = AmqpEventImpl.builder()
            .erro(ErrorResponse.builder().build()).build();
        amqpEventUnwrapper.verify(event);
    }

}
