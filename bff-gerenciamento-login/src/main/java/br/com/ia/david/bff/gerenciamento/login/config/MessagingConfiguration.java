package br.com.ia.david.bff.gerenciamento.login.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.ia.david.bff.gerenciamento.login.domain.messaging.Messaging;

@Configuration
public class MessagingConfiguration {

    /**
     * Exchanges
     **/

    //@Bean
    TopicExchange exchange() {
        return new TopicExchange(Messaging.EXCHANGE);
    }

    //@Bean
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
    Queue receivedEventsQueue() {
        return new Queue(Messaging.QUEUE_RECEIVED_EVENTS);
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
    Binding inserirLoginSucessoQueueToExchangeBinder() {
        return BindingBuilder.bind(receivedEventsQueue())
            .to(eventsExchange())
            .with(Messaging.INSERIR_LOGIN_SUCCESS.getRoutingKey());
    }

    @Bean
    Binding inserirLoginErroQueueToExchangeBinder() {
        return BindingBuilder.bind(receivedEventsQueue())
            .to(eventsExchange())
            .with(Messaging.INSERIR_LOGIN_ERROR.getRoutingKey());
    }
}
