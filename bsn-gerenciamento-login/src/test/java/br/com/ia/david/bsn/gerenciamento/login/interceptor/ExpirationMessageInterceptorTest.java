package br.com.ia.david.bsn.gerenciamento.login.interceptor;

import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties.Template;

@RunWith(MockitoJUnitRunner.class)
public class ExpirationMessageInterceptorTest {

    @InjectMocks
    private ExpirationMessageInterceptor interceptor;

    @Mock
    private RabbitProperties rabbitProperties;

    @Mock
    private Template template;

    private Duration timeout;
    private MessageBuilder messageBuilder;

    @Before
    public void setup() {

        timeout = Duration.of(nextLong(), ChronoUnit.MILLIS);
        messageBuilder = MessageBuilder.withBody(UUID.randomUUID().toString().getBytes());

        when(template.getReplyTimeout()).thenReturn(timeout);
        when(rabbitProperties.getTemplate()).thenReturn(template);
    }

    @Test
    public void naoHaReplyTo_naoDeveTerExpiration() {

        messageBuilder.setReplyTo(null);

        final Message mpp = interceptor.postProcessMessage(messageBuilder.build());
        assertNotNull(mpp);
        assertNull(mpp.getMessageProperties().getExpiration());

        verifyNoInteractions(rabbitProperties, template);
    }

    @Test
    public void naoHaReplyToTemplate_naoDeveTerExpiration() {

        when(template.getReplyTimeout()).thenReturn(null);

        messageBuilder.setReplyTo(UUID.randomUUID().toString());

        final Message mpp = interceptor.postProcessMessage(messageBuilder.build());
        assertNotNull(mpp);
        assertNull(mpp.getMessageProperties().getExpiration());

        verify(rabbitProperties).getTemplate();
        verify(template).getReplyTimeout();
    }

    @Test
    public void haReplyTo_deveTerExpiration() {

        messageBuilder.setReplyTo(UUID.randomUUID().toString());

        final Message mpp = interceptor.postProcessMessage(messageBuilder.build());
        assertNotNull(mpp);
        assertEquals(String.valueOf(timeout.toMillis()), mpp.getMessageProperties().getExpiration());

        verify(rabbitProperties).getTemplate();
        verify(template).getReplyTimeout();
    }
}