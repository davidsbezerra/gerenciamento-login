package br.com.ia.david.bff.gerenciamento.login.interceptor;

import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Random;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import lombok.AccessLevel;
import lombok.Getter;

@Component
public class TraceMessageInterceptor implements MethodBeforeAdvice, MessagePostProcessor {

    private static final String TRACE_ID = "X-B3-TraceId";
    private static final String SPAN_ID = "X-B3-SpanId";

    @Getter(lazy = true, value = AccessLevel.PRIVATE)
    private final Random random = new Random();

    @Autowired
    private Tracer tracer;

    /**
     * Invocado automaticamente pelo Spring Advice Chain antes de uma mensagem entrar no listener.
     *
     * <br/><br/>Se a mensagem tiver um cabeçalho com nome {@link #TRACE_ID} e o valor for numérico, este valor será
     * convertido na sua representação textual, tendo também a representação equivalente de {@link #SPAN_ID}
     * adicionado como cabeçalho da mensagem.
     *
     * <br/><br/>Se não houver o cabeçalho, nada será feito na mensagem.
     */
    @Override
    public void before(final Method method, final Object[] args, final Object target) throws Throwable {

        final Message message = (Message) args[1];
        final Map<String, Object> headers = message.getMessageProperties().getHeaders();

        ofNullable(headers.get(TRACE_ID))
            .filter(Number.class::isInstance)
            .map(Long.class::cast)
            .map(traceId -> TraceContext.newBuilder()
                .traceId(traceId)
                .spanId(getRandom().nextLong())
                .build())
            .ifPresent(traceContext -> {
                headers.put(TRACE_ID, traceContext.traceIdString());
                headers.put(SPAN_ID, traceContext.spanIdString());
            });
    }

    /**
     * Invocado automaticamente pelo RabbitTemplate antes de enviar uma mensagem para o broker.
     *
     * <br/><br/>Serve para verificar se a mensagem possui o cabeçalho {@link #TRACE_ID} e, em caso negativo,
     * adicionar.
     *
     * <br/><br/>O valor do cabeçalho será a representação numérica do Trace ID do span corrente. Caso não haja span
     * corrente, será gerado um novo de forma randômica.
     */
    @Override
    public Message postProcessMessage(final Message message) throws AmqpException {

        final Map<String, Object> headers = message.getMessageProperties().getHeaders();

        if (headers.get(TRACE_ID) == null) {

            final Span span = ofNullable(tracer.currentSpan())
                .orElseGet(() -> tracer.nextSpan().name(randomUUID().toString()).start());

            headers.put(TRACE_ID, span.context().traceId());
        }

        return message;
    }
}
