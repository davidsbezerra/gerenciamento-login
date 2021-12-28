package br.com.ia.david.bsn.gerenciamento.login.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.ia.david.bsn.gerenciamento.login.domain.messaging.Messaging;
import br.com.ia.david.bsn.gerenciamento.login.event.RemoverLoginAmqpEvent;
import br.com.ia.david.bsn.gerenciamento.login.message.RemoverLoginMessage;
import br.com.ia.david.bsn.gerenciamento.login.service.InserirLoginService;
import br.com.ia.david.bsn.gerenciamento.login.service.RemoverLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@RabbitListener(queues = Messaging.QUEUE_REMOVER_LOGIN)
public class RemoverLojaTotvsGiaListener {

    private final AmqpEventListenerHandler handler;
    private final RemoverLoginService service;

    @RabbitHandler
    void receive(@Payload final RemoverLoginMessage message, @Headers final MessageHeaders headers) {

        log.debug("Mensagem {}", message);

        handler.handle(headers,
            message,
            RemoverLoginAmqpEvent.class,
            () -> service.remover(message));
    }
}