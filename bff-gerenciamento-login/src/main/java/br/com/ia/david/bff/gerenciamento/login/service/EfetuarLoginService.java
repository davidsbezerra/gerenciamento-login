package br.com.ia.david.bff.gerenciamento.login.service;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import br.com.ia.david.bff.gerenciamento.login.domain.login.Login;
import br.com.ia.david.bff.gerenciamento.login.domain.messaging.Messaging;
import br.com.ia.david.bff.gerenciamento.login.event.AmqpEventUnwrapper;
import br.com.ia.david.bff.gerenciamento.login.message.EfetuarLoginMessage;
import br.com.ia.david.bff.gerenciamento.login.request.EfetuarLoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EfetuarLoginService {

    private final RabbitTemplate rabbitTemplate;

    private final AmqpEventUnwrapper amqpEventUnwrapper;

    public void efetuar(final EfetuarLoginRequest request) {

        //TODO: inserir validador

        log.info("Efetuando login {}", request.getLogin());

        final EfetuarLoginMessage message = EfetuarLoginMessage.builder()
            .login(
                Login.builder()
                    .login(request.getLogin())
                    .password(request.getPassword())
                    .build()
            ).build();

        rabbitTemplate.convertAndSend(
            Messaging.EFETUAR_LOGIN.getExchange(),
            Messaging.EFETUAR_LOGIN.getRoutingKey(),
            message
        );
    }
}
