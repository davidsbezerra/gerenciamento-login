package br.com.ia.david.bsn.gerenciamento.login.domain.messaging;

/**
 * Interface para definição de nomes de exchanges, queues e routing keys.
 */
public interface Messaging {

    MessageInbox INSERIR_LOGIN = new MessageInbox("gerenciamento-login.inserir.message");
    MessageInbox REMOVER_LOGIN = new MessageInbox("gerenciamento-login.remover.message");
    MessageInbox ATUALIZAR_LOGIN = new MessageInbox("gerenciamento-login.atualizar.message");
    MessageInbox EFETUAR_LOGIN = new MessageInbox("gerenciamento-login.efetuar.message");
    MessageInbox ENVIAR_COMUNICADO = new MessageInbox("gerenciamento-login.enviar-comunicado.message");

    MessageOutbox GFE = new MessageOutbox("gfe.armazenar-mensagem-gfe.message");

    String QUEUE_INSERIR_LOGIN = "gerenciamento-login.inserir.queue";
    String QUEUE_REMOVER_LOGIN = "gerenciamento-login.remover.queue";
    String QUEUE_ATUALIZAR_LOGIN = "gerenciamento-login.atualizar.queue";
    String QUEUE_EFETUAR_LOGIN = "gerenciamento-login.efetuar.queue";
    String QUEUE_ENVIAR_COMUNICADO = "gerenciamento-login.enviar-comunicado.queue";

    String INSERIR_LOGIN_SUCCESS = "gerenciamento-login.inserir.success.event";
    String INSERIR_LOGIN_ERROR = "gerenciamento-login.inserir.error.event";
    String REMOVER_LOGIN_SUCCESS = "gerenciamento-login.remover.success.event";
    String REMOVER_LOGIN_ERROR = "gerenciamento-login.remover.error.event";
    String ATUALIZAR_LOGIN_SUCCESS = "gerenciamento-login.atualizar.success.event";
    String ATUALIZAR_LOGIN_ERROR = "gerenciamento-login.atualizar.error.event";
    String EFETUAR_LOGIN_SUCCESS = "gerenciamento-login.efetuar.success.event";
    String EFETUAR_LOGIN_ERROR = "gerenciamento-login.efetuar.error.event";
    String ENVIAR_COMUNICADO_SUCCESS = "gerenciamento-login.enviar-comunicado.success.event";
    String ENVIAR_COMUNICADO_ERROR = "gerenciamento-login.enviar-comunicado.error.event";

    String AMQ_TOPIC = "amq.topic";

    String EXCHANGE = "gerenciamento-login.exchange";
    String EXCHANGE_EVENTS = "gerenciamento-login.events.exchange";

    String EXCHANGE_REPLY_TO = "exchange.reply-to"; // Indica a exchange que deve receber a resposta
    String ROUTE_REPLY_TO = "route.reply-to"; // Indica a rota que deve receber a resposta
    
    String QUEUE_GFE = "gfe.armazenar-mensagem-gfe.queue";

}
