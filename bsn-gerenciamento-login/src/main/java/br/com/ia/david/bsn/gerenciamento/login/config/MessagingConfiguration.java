package br.com.ia.david.bsn.gerenciamento.login.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.ia.david.bsn.gerenciamento.login.domain.messaging.Messaging;

@Configuration
public class MessagingConfiguration {

    /**
     * Exchanges
     **/

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(Messaging.EXCHANGE);
    }

    @Bean
    TopicExchange eventsExchange() {
        return new TopicExchange(Messaging.EXCHANGE_EVENTS);
    }


    /**
     * Queues
     **/

    @Bean
    public Queue gfeQueue() {
        return new Queue(Messaging.QUEUE_GFE);
    }

    @Bean
    Queue inserirLoginQueue() {
        return new Queue(Messaging.QUEUE_INSERIR_LOGIN);
    }

    @Bean
    Queue removerLoginQueue() {
        return new Queue(Messaging.QUEUE_REMOVER_LOGIN);
    }

    @Bean
    Queue atualizarLoginQueue() {
        return new Queue(Messaging.QUEUE_ATUALIZAR_LOGIN);
    }

    /**
     * Bindings
     */

    @Bean
    Binding gfeQueueToAmqTopicExchangeBinder() {
        return BindingBuilder.bind(gfeQueue())
            .to(new TopicExchange(Messaging.AMQ_TOPIC))
            .with(Messaging.GFE.getRoutingKey());
    }

    @Bean
    Binding inserirLoginQueueToEventsExchangeBinder() {
        return BindingBuilder.bind(inserirLoginQueue())
            .to(exchange())
            .with(Messaging.INSERIR_LOGIN.getRoutingKey());
    }

    @Bean
    Binding removerLoginQueueToEventsExchangeBinder() {
        return BindingBuilder.bind(removerLoginQueue())
            .to(exchange())
            .with(Messaging.REMOVER_LOGIN.getRoutingKey());
    }

    @Bean
    Binding atualizarLoginQueueToEventsExchangeBinder() {
        return BindingBuilder.bind(atualizarLoginQueue())
            .to(exchange())
            .with(Messaging.ATUALIZAR_LOGIN.getRoutingKey());
    }
}