package br.com.ia.david.bsn.gerenciamento.login.domain.messaging;

/**
 * Interface para definição de nomes de exchanges, queues e routing keys.
 */
public interface Messaging {

    MessageInbox INSERIR_LOGIN = new MessageInbox("gerenciamento-login.inserir.message");
    MessageInbox REMOVER_LOGIN = new MessageInbox("gerenciamento-login.remover.message");

    MessageOutbox GFE = new MessageOutbox("gfe.armazenar-mensagem-gfe.message");

    String QUEUE_INSERIR_LOGIN = "gerenciamento-login.inserir.queue";
    String QUEUE_REMOVER_LOGIN = "gerenciamento-login.remover.queue";

    String INSERIR_LOGIN_SUCCESS = "gerenciamento-login.inserir.success.event";
    String INSERIR_LOGIN_ERROR = "gerenciamento-login.inserir.error.event";
    String REMOVER_LOGIN_SUCCESS = "gerenciamento-login.remover.success.event";
    String REMOVER_LOGIN_ERROR = "gerenciamento-login.remover.error.event";

    String AMQ_TOPIC = "amq.topic";

    String EXCHANGE = "gerenciamento-login.exchange";
    String EXCHANGE_EVENTS = "gerenciamento-login.events.exchange";

    String EXCHANGE_REPLY_TO = "exchange.reply-to"; // Indica a exchange que deve receber a resposta
    String ROUTE_REPLY_TO = "route.reply-to"; // Indica a rota que deve receber a resposta
    
    String QUEUE_GFE = "gfe.armazenar-mensagem-gfe.queue";

}
