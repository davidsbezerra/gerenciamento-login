package br.com.ia.david.bff.gerenciamento.login.listener.receiver;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.ia.david.bff.gerenciamento.login.domain.messaging.Messaging;
import br.com.ia.david.bff.gerenciamento.login.event.InserirLoginAmqpEvent;
import br.com.ia.david.bff.gerenciamento.login.event.InserirLoginAmqpEvent.InserirLoginResponse;
import br.com.ia.david.bff.gerenciamento.login.redis.entity.LoginEntity;
import br.com.ia.david.bff.gerenciamento.login.redis.repository.LoginEntityRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RabbitListener(queues = Messaging.QUEUE_RECEIVED_EVENTS)
@Slf4j
@RequiredArgsConstructor
public class ReceivedEventListener {

    @Value("${redis.gerenciamento-login.ttl}")
    private Long ttlLogin;

    private final LoginEntityRedisRepository repository;

    @RabbitHandler
    void receive(final InserirLoginAmqpEvent event) {

        log.info("Evento: {}", event);
        if (nonNull(event) && isNull(event.getErro())) {

            ofNullable(event.getResultado()).map(InserirLoginResponse::getLogin)
                .ifPresent(l -> repository.save(
                    LoginEntity.builder()
                        .ttl(ttlLogin)
                        .id(l.getId())
                        .name(l.getName())
                        .password(l.getPassword())
                        .build()
                ));
        }
    }
}
