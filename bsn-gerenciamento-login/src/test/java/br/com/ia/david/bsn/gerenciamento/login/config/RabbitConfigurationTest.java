package br.com.ia.david.bsn.gerenciamento.login.config;

import org.mockito.Mockito;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitConfigurationTest {

    @Primary @Bean
    public ConnectionFactory connectionFactory() {
        return Mockito.mock(ConnectionFactory.class);
    }

    @Primary @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory() {
        return Mockito.mock(SimpleRabbitListenerContainerFactory.class);
    }

    @Primary @Bean
    public RabbitProperties rabbitProperties() {
        return new RabbitProperties();
    }

}
