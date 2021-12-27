package br.com.ia.david.bsn.gerenciamento.login.domain.messaging;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class EventOutboxTest {

    @Test
    public void criarEventOutboxComExchangeAndRoutingKey() {
        String exchange = RandomStringUtils.randomAlphabetic(10);
        String key = RandomStringUtils.randomAlphabetic(10);
        String suffix = RandomStringUtils.randomAlphabetic(10);

        String routingKey = generateRoutingKey(exchange, key, suffix);

        EventOutbox config = new EventOutbox(routingKey);

        assertEquals(routingKey, config.getRoutingKey());
        assertEquals(String.format("%s.events.exchange", exchange), config.getExchange());
    }

    private String generateRoutingKey(String exchange, String key, String suffix) {
        return String.format("%s.%s.%s", exchange, key, suffix);

    }
}