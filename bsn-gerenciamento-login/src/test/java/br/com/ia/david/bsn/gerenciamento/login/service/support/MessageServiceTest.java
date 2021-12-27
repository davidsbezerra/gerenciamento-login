package br.com.ia.david.bsn.gerenciamento.login.service.support;

import static br.com.ia.david.bsn.gerenciamento.login.domain.Message.SERVICO_INDISPONIVEL;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageSource messageSource;

    private String stringRandomica;
    private Locale pt_BR;

    @Before
    public void setup() {
        stringRandomica = UUID.randomUUID().toString();
        pt_BR = new Locale("pt", "BR");
        when(messageSource.getMessage(eq(SERVICO_INDISPONIVEL.getMessage()), any(), eq(pt_BR))).thenReturn(stringRandomica);
    }

    @Test
    public void verificarChamadaMessageSource() {
        final String resultadoAtual = messageService.get(SERVICO_INDISPONIVEL);
        assertEquals(stringRandomica, resultadoAtual);
        verify(messageSource).getMessage(SERVICO_INDISPONIVEL.getMessage(), new Object[]{}, pt_BR);
    }

    @Test
    public void verificarChamadaMessageSourceComParams() {
        final String resultadoAtual = messageService.get(SERVICO_INDISPONIVEL, "Teste");
        assertEquals(stringRandomica, resultadoAtual);
        verify(messageSource).getMessage(SERVICO_INDISPONIVEL.getMessage(), new Object[]{"Teste"}, pt_BR);
    }

    @Test
    public void verificarMessageSourceGetNull() {
        assertEquals("", messageService.get(null));
        verifyNoInteractions(messageSource);
    }
}
