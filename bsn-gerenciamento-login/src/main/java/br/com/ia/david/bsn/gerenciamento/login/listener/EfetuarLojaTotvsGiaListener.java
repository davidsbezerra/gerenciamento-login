package br.com.ia.david.bsn.gerenciamento.login.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.ia.david.bsn.gerenciamento.login.domain.messaging.Messaging;
import br.com.ia.david.bsn.gerenciamento.login.event.EfetuarLoginAmqpEvent;
import br.com.ia.david.bsn.gerenciamento.login.message.EfetuarLoginMessage;
import br.com.ia.david.bsn.gerenciamento.login.service.EfetuarLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@RabbitListener(queues = Messaging.QUEUE_EFETUAR_LOGIN)
public class EfetuarLojaTotvsGiaListener {

    private final AmqpEventListenerHandler handler;
    private final EfetuarLoginService service;

    @RabbitHandler
    void receive(@Payload final EfetuarLoginMessage message, @Headers final MessageHeaders headers) {

        log.debug("Mensagem {}", message);

        handler.handle(headers,
            message,
            EfetuarLoginAmqpEvent.class,
            () -> service.efetuar(message));
    }
}