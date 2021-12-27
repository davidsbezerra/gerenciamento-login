package br.com.ia.david.bff.gerenciamento.login.listener;

import static java.util.Optional.ofNullable;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.amqp.core.Address;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.ia.david.bff.gerenciamento.login.domain.messaging.Messaging;
import br.com.ia.david.bff.gerenciamento.login.event.AmqpEvent;
import br.com.ia.david.bff.gerenciamento.login.response.ErrorResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
class AmqpEventListenerHandler {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    <T extends Serializable, R extends Serializable, E extends AmqpEvent<T, R>> void handle(
        final Map<String, Object> headers,
        final T request, 
        final Class<E> responseClass,
        final Supplier<R> logic) {

        final E response = newInstanceOf(responseClass);

        try {
            final R result = logic.get();
            response.setResultado(result);

        } catch (final Exception e) { // NOSONAR
            final ErrorResponse errorResponse = ErrorResponse.build(e);
            response.setErro(errorResponse);
        }

        final Optional<Address> replyTo = ofNullable(headers.get(AmqpHeaders.REPLY_TO))
            .map(String::valueOf)
            .map(Address::new);

        if (replyTo.isPresent()) {

            response.setRequisicao(null);

            final Optional<String> correlationId = ofNullable(headers.get(AmqpHeaders.CORRELATION_ID))
                .map(String::valueOf);

            rabbitTemplate.convertAndSend(replyTo.get().getExchangeName(), replyTo.get().getRoutingKey(), response, mpp -> {
                correlationId.ifPresent(cid -> mpp.getMessageProperties().setCorrelationId(cid));
                return mpp;
            });
        } else {

            final String outboxRoutingKey = ofNullable(response.getErro())
                .map(e -> response.getErrorRoute())
                .orElseGet(response::getSuccessRoute);

            final String exchange = ofNullable(headers.get(Messaging.EXCHANGE_REPLY_TO))
                .map(String::valueOf)
                .orElse(Messaging.EXCHANGE_EVENTS);

            final String route = ofNullable(headers.get(Messaging.ROUTE_REPLY_TO))
                .map(String::valueOf)
                .orElse(outboxRoutingKey);

            response.setRequisicao(request);
            rabbitTemplate.convertAndSend(exchange, route, response);
        }
    }

    @SneakyThrows
    private static <T> T newInstanceOf(final Class<T> clasz) {
        return clasz.newInstance();
    }
}
