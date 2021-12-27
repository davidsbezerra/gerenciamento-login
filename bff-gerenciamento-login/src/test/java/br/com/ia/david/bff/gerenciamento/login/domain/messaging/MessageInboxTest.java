package br.com.ia.david.bff.gerenciamento.login.domain.messaging;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class MessageInboxTest {

    @Test
    public void messageInboxComExchangeERoutingKey() {
        String exchange = RandomStringUtils.randomAlphabetic(10);
        String key = RandomStringUtils.randomAlphabetic(10);
        String suffix = RandomStringUtils.randomAlphabetic(10);

        String routingKey = generateRoutingKey(exchange, key, suffix);

        MessageInbox config = new MessageInbox(routingKey);

        assertEquals(routingKey, config.getRoutingKey());
        assertEquals(String.format("%s.%s.queue", exchange, key), config.getQueue());
        assertEquals(String.format("%s.exchange", exchange), config.getExchange());
    }

    @Test
    public void messageInboxComRoutingKeyProcessadora() {

        final MessageInbox message = new MessageInbox("exchange.command.processadora.message");
        assertEquals("exchange.command.processadora.queue", message.getQueue());
    }

    @Test
    public void messageInboxComRoutingKeySemProcessadora() {

        final MessageInbox message = new MessageInbox("exchange.command.message");
        assertEquals("exchange.command.queue", message.getQueue());
    }

    private String generateRoutingKey(String exchange, String key, String suffix) {
        return String.format("%s.%s.%s", exchange, key, suffix);

    }
}