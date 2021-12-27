package br.com.ia.david.bff.gerenciamento.login.domain.messaging;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class MessageOutboxTest {

    @Test
    public void criarEventOutboxComExchangeAndRoutingKey() {
        String exchange = RandomStringUtils.randomAlphabetic(10);
        String key = RandomStringUtils.randomAlphabetic(10);
        String suffix = RandomStringUtils.randomAlphabetic(10);

        String routingKey = generateRoutingKey(exchange, key, suffix);

        MessageOutbox config = new MessageOutbox(routingKey);

        assertEquals(routingKey, config.getRoutingKey());
        assertEquals(String.format("%s.exchange", exchange), config.getExchange());
    }

    private String generateRoutingKey(String exchange, String key, String suffix) {
        return String.format("%s.%s.%s", exchange, key, suffix);

    }
}