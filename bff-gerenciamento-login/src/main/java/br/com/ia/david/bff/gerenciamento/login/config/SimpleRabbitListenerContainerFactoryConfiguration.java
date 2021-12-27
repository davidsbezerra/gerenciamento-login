package br.com.ia.david.bff.gerenciamento.login.config;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.annotation.PostConstruct;

import org.aopalliance.aop.Advice;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import br.com.ia.david.bff.gerenciamento.login.interceptor.HeaderMessageInterceptor;
import br.com.ia.david.bff.gerenciamento.login.interceptor.ListenerExceptionInterceptor;
import br.com.ia.david.bff.gerenciamento.login.interceptor.TraceMessageInterceptor;

@Configuration
public class SimpleRabbitListenerContainerFactoryConfiguration {

    @Autowired
    private SimpleRabbitListenerContainerFactory listenerFactory;

    @Autowired
    private HeaderMessageInterceptor headerMessageInterceptor;

    @Autowired
    private ListenerExceptionInterceptor listenerExceptionInterceptor;

    @Autowired
    private TraceMessageInterceptor traceMessageInterceptor;

    @PostConstruct
    void simpleRabbitListenerContainerFactorySetup() {

        final Advice[] advices = ofNullable(listenerFactory.getAdviceChain()).orElse(new Advice[0]);

        final List<Advice> chain = stream(advices).collect(toList());
        chain.add(headerMessageInterceptor);
        chain.add(listenerExceptionInterceptor);
        chain.add(0, traceMessageInterceptor); // precisa ser o primeiro da lista

        listenerFactory.setAdviceChain(chain.toArray(new Advice[0]));
    }
}
