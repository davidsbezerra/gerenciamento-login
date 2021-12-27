package br.com.ia.david.bff.gerenciamento.login.config;

import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ia.david.bff.gerenciamento.login.annotations.AmqpObject;
import br.com.ia.david.bff.gerenciamento.login.annotations.EventTemplate;
import br.com.ia.david.bff.gerenciamento.login.domain.messaging.Messaging;
import br.com.ia.david.bff.gerenciamento.login.interceptor.ExpirationMessageInterceptor;
import br.com.ia.david.bff.gerenciamento.login.interceptor.HeaderMessageInterceptor;
import br.com.ia.david.bff.gerenciamento.login.interceptor.TraceMessageInterceptor;

@Configuration
public class RabbitTemplateConfiguration {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private RabbitProperties rabbitProperties;

    @Autowired
    private ExpirationMessageInterceptor expirationMessageInterceptor;

    @Autowired
    private HeaderMessageInterceptor headerMessageInterceptor;

    @Autowired
    private TraceMessageInterceptor traceMessageInterceptor;

    @Bean
    Jackson2JsonMessageConverter objectToJsonMessageConverter() {

        final Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        converter.setClassMapper(jsonClassMapper());
        return converter;
    }

    @Bean
    DefaultClassMapper jsonClassMapper() {

        final Map<String, Class<?>> mapping = new HashMap<>();
        final String mainPackage = substringBeforeLast(getClass().getPackage().getName(), ".");

        new Reflections(mainPackage)
            .getTypesAnnotatedWith(AmqpObject.class)
            .forEach(clazz -> mapping.put(clazz.getSimpleName(), clazz));

        final DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setIdClassMapping(mapping);
        return classMapper;
    }


    /**
     * Cria uma instância de RabbitTemplate com o conversor de json e as informações do binder.
     */
    private RabbitTemplate createRabbitTemplate() {

        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.addBeforePublishPostProcessors(
            expirationMessageInterceptor, headerMessageInterceptor, traceMessageInterceptor);
        rabbitTemplate.setMessageConverter(objectToJsonMessageConverter());
        rabbitTemplate.setReplyTimeout(rabbitProperties.getTemplate().getReplyTimeout().toMillis());

        return rabbitTemplate;
    }


    /**
     * Rabbit Templates
     **/

    @Bean
    @Primary
    RabbitTemplate rabbitTemplate() {
        return createRabbitTemplate();
    }

    //@Bean
    //@EventTemplate
    RabbitTemplate eventTemplate() {

        final RabbitTemplate rabbitTemplate = createRabbitTemplate();
        rabbitTemplate.setExchange(Messaging.EXCHANGE_EVENTS);
        return rabbitTemplate;
    }
}
