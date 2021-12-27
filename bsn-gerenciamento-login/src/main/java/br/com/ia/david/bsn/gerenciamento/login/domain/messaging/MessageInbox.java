package br.com.ia.david.bsn.gerenciamento.login.domain.messaging;

import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

import lombok.Getter;

/**
 * Classe utilitária para definir as configurações de recebimento de mensagens
 *   de acordo com o padrão de mensagem definido no projeto.
 *
 * Padrão de routingKey de message: "consumer.chave-da-mensagem.message" ou "consumer.chave-da-mensagem.processadora.message"
 * Padrão de exchange de message: "consumer.exchange"
 * Padrão de queue: "consumer.chave-da-mensagem.queue" ou "consumer.chave-da-mensagem.processadora.queue"
 */
@Getter
public class MessageInbox {

    private final String queue;

    private final String exchange;

    private final String routingKey;

    public MessageInbox(String routingKey) {
        this.routingKey = routingKey;
        this.exchange = String.format("%s.exchange", prefix(routingKey));
        this.queue = String.format("%s.%s.queue", prefix(routingKey), message(routingKey));
    }

    private String message(String routingKey) {
        return substringBeforeLast(substringAfter(routingKey, "."), ".");
    }

    private String prefix(String routingKey) {
        return routingKey.split("\\.")[0];
    }

}
