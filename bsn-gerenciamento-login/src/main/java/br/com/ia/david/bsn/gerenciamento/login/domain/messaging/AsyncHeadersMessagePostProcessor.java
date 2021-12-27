package br.com.ia.david.bsn.gerenciamento.login.domain.messaging;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AsyncHeadersMessagePostProcessor implements MessagePostProcessor {

    private final String route;

    @Override
    public Message postProcessMessage(Message message) {
        message.getMessageProperties().getHeaders().put(Messaging.EXCHANGE_REPLY_TO, Messaging.EXCHANGE_EVENTS);
        message.getMessageProperties().getHeaders().put(Messaging.ROUTE_REPLY_TO, route);
        return message;
    }

}
