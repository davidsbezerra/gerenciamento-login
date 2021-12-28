package br.com.ia.david.bff.gerenciamento.login.service;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import br.com.ia.david.bff.gerenciamento.login.domain.login.Login;
import br.com.ia.david.bff.gerenciamento.login.domain.messaging.Messaging;
import br.com.ia.david.bff.gerenciamento.login.event.AmqpEventUnwrapper;
import br.com.ia.david.bff.gerenciamento.login.event.InserirLoginAmqpEvent;
import br.com.ia.david.bff.gerenciamento.login.message.InserirLoginMessage;
import br.com.ia.david.bff.gerenciamento.login.request.InserirLoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BuscarLoginService {


    private final RabbitTemplate rabbitTemplate;

    private final AmqpEventUnwrapper amqpEventUnwrapper;

    public Login inserir(final InserirLoginRequest request) {

        //validator.accept(request);

        log.info("Inserindo login {}", request.getLogin());

        final InserirLoginMessage message = InserirLoginMessage.builder()
            .login(
                Login.builder()
                    .login(request.getLogin())
                    .password(request.getPassword())
                    .admin(request.getAdmin())
                    .email(request.getEmail())
                    .createdDate(request.getCreatedDate())
                    .updatedDate(request.getUpdatedDate())
                    .build()
            ).build();

        return amqpEventUnwrapper.verify((InserirLoginAmqpEvent)
            rabbitTemplate.convertSendAndReceive(
                Messaging.INSERIR_LOGIN.getExchange(),
                Messaging.INSERIR_LOGIN.getRoutingKey(),
                message
            )).getLogin();
    }
}
