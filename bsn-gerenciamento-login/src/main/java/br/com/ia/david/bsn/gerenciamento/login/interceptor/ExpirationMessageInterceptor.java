package br.com.ia.david.bsn.gerenciamento.login.interceptor;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Objects;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties.Template;
import org.springframework.stereotype.Component;

@Component
public class ExpirationMessageInterceptor implements MessagePostProcessor {

    @Autowired
    private RabbitProperties rabbitProperties;

    /**
     * Invocado automaticamente pelo RabbitTemplate antes de enviar uma mensagem para o broker.
     *
     * <br/><br/>Caso a mensagem possua header de resposta <em>(o que indica que é um request-reply)</em>,
     *   seta o tempo de expiração da mensagem para o mesmo tempo de timeout configurado no RabbitTemplate.
     */
    @Override
    public Message postProcessMessage(final Message message) throws AmqpException {

        if (isNotBlank(message.getMessageProperties().getReplyTo())) {

            ofNullable(rabbitProperties.getTemplate())
                .map(Template::getReplyTimeout)
                .filter(Objects::nonNull)
                .map(timeout -> timeout.toMillis())
                .map(String::valueOf)
                .ifPresent(timeout -> message.getMessageProperties().setExpiration(timeout));
        }

        return message;
    }
}
