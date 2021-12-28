package br.com.ia.david.bsn.gerenciamento.login.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.ia.david.bsn.gerenciamento.login.domain.messaging.Messaging;
import br.com.ia.david.bsn.gerenciamento.login.event.InserirLoginAmqpEvent;
import br.com.ia.david.bsn.gerenciamento.login.message.InserirLoginMessage;
import br.com.ia.david.bsn.gerenciamento.login.service.InserirLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@RabbitListener(queues = Messaging.QUEUE_INSERIR_LOGIN)
public class InserirLojaTotvsGiaListener {

    private final AmqpEventListenerHandler handler;
    private final InserirLoginService service;

    @RabbitHandler
    void receive(@Payload final InserirLoginMessage message, @Headers final MessageHeaders headers) {

        log.debug("Mensagem {}", message);

        handler.handle(headers,
            message,
            InserirLoginAmqpEvent.class,
            () -> service.inserir(message));
    }
}