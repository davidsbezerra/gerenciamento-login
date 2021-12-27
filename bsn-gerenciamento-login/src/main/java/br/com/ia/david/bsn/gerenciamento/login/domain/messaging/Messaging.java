package br.com.ia.david.bsn.gerenciamento.login.domain.messaging;

/**
 * Interface para definição de nomes de exchanges, queues e routing keys.
 */
public interface Messaging {

    MessageOutbox GFE = new MessageOutbox("gfe.armazenar-mensagem-gfe.message");

    String AMQ_TOPIC = "amq.topic";

    String EXCHANGE = "gerenciamento-login.exchange";
    String EXCHANGE_EVENTS = "gerenciamento-login.events.exchange";

    String EXCHANGE_REPLY_TO = "exchange.reply-to"; // Indica a exchange que deve receber a resposta
    String ROUTE_REPLY_TO = "route.reply-to"; // Indica a rota que deve receber a resposta
    
    String QUEUE_GFE = "gfe.armazenar-mensagem-gfe.queue";

}
