package br.com.ia.david.bff.gerenciamento.login.service;


import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import br.com.ia.david.bff.gerenciamento.login.domain.ErrorType;
import br.com.ia.david.bff.gerenciamento.login.domain.Message;
import br.com.ia.david.bff.gerenciamento.login.domain.messaging.Messaging;
import br.com.ia.david.bff.gerenciamento.login.exception.ClientErrorException;
import br.com.ia.david.bff.gerenciamento.login.message.EnviarComunicadoMessage;
import br.com.ia.david.bff.gerenciamento.login.service.support.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnviarComunicadoService {

    private final RabbitTemplate rabbitTemplate;
    private final MessageService messageService;

    public void enviar(final String mensagem, String usuario) {

        if (StringUtils.isBlank(mensagem)) {
            throw new ClientErrorException(ErrorType.BUSINESS,
                messageService.get(Message.NENHUM_RESULTADO_ENCONTRADO));
        }

        log.info("Enviando comunicado para " + (StringUtils.isBlank(usuario) ? "todos usuarios admin." : "usuario {}."),
            usuario);

        final EnviarComunicadoMessage message = EnviarComunicadoMessage.builder()
            .mensagem(mensagem)
            .login(usuario)
            .build();

        rabbitTemplate.convertAndSend(
            Messaging.ENVIAR_COMUNICADO.getExchange(),
            Messaging.ENVIAR_COMUNICADO.getRoutingKey(),
            message
        );
    }
}
