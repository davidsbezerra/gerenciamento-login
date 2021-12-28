package br.com.ia.david.bff.gerenciamento.login.domain.messaging;

/**
 * Interface para definição de nomes de exchanges, queues e routing keys.
 */
public interface Messaging {

    MessageOutbox GFE = new MessageOutbox("gfe.armazenar-mensagem-gfe.message");

    EventInbox INSERIR_LOGIN_SUCCESS = new EventInbox("gerenciamento-login.inserir.success.event");
    EventInbox INSERIR_LOGIN_ERROR = new EventInbox("gerenciamento-login.inserir.error.event");
    EventInbox EFETUAR_LOGIN_SUCCESS = new EventInbox("gerenciamento-login.efetuar.success.event");
    EventInbox EFETUAR_LOGIN_ERROR = new EventInbox("gerenciamento-login.efetuar.error.event");
    EventInbox REMOVER_LOGIN_SUCCESS = new EventInbox("gerenciamento-login.remover.success.event");
    EventInbox REMOVER_LOGIN_ERROR = new EventInbox("gerenciamento-login.remover.error.event");
    EventInbox ATUALIZAR_LOGIN_SUCCESS = new EventInbox("gerenciamento-login.atualizar.success.event");
    EventInbox ATUALIZAR_LOGIN_ERROR = new EventInbox("gerenciamento-login.atualizar.error.event");

    String AMQ_TOPIC = "amq.topic";

    String EXCHANGE = "gerenciamento-login.exchange";
    String EXCHANGE_EVENTS = "gerenciamento-login.events.exchange";

    String EXCHANGE_REPLY_TO = "exchange.reply-to"; // Indica a exchange que deve receber a resposta
    String ROUTE_REPLY_TO = "route.reply-to"; // Indica a rota que deve receber a resposta

    String QUEUE_GFE = "gfe.armazenar-mensagem-gfe.queue";

    MessageOutbox INSERIR_LOGIN = new MessageOutbox("gerenciamento-login.inserir.message");
    MessageOutbox EFETUAR_LOGIN = new MessageOutbox("gerenciamento-login.efetuar.message");
    MessageOutbox ENVIAR_COMUNICADO = new MessageOutbox("gerenciamento-login.enviar-comunicado.message");

    String QUEUE_RECEIVED_EVENTS = "gerenciamento-login.received-events.queue";
}
