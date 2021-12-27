package br.com.ia.david.bsn.gerenciamento.login.listener;

import static org.apache.commons.lang3.RandomUtils.nextBoolean;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.core.Address;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;

import br.com.ia.david.bsn.gerenciamento.login.domain.messaging.EventOutbox;
import br.com.ia.david.bsn.gerenciamento.login.domain.messaging.Messaging;
import br.com.ia.david.bsn.gerenciamento.login.event.AmqpEvent;
import br.com.ia.david.bsn.gerenciamento.login.response.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@RunWith(MockitoJUnitRunner.class)
public class AmqpEventListenerHandlerTest {

    private static final EventOutbox SUCCESS_EVENT = new EventOutbox("amqp.success.event");
    private static final EventOutbox ERROR_EVENT = new EventOutbox("amqp.error.event");

    @InjectMocks
    private AmqpEventListenerHandler handler;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Spy
    private BusinessLogic businessLogic;

    @Captor
    private ArgumentCaptor<AmqpEventObject> amqpEventCaptor;

    @Captor
    private ArgumentCaptor<MessagePostProcessor> mppCaptor;

    private Map<String, Object> headers;
    private RequestObject request;
    private Class<AmqpEventObject> responseClass;
    private Boolean hasCorrelationId;
    private Long correlationId;

    @Before
    public void setup() {

        headers = new HashMap<>();
        request = RequestObject.builder().id(UUID.randomUUID().toString()).build();
        responseClass = AmqpEventObject.class;
        hasCorrelationId = nextBoolean();

        if (hasCorrelationId) {
            correlationId = nextLong();
            headers.put(AmqpHeaders.CORRELATION_ID, correlationId);
        }
    }

    @Test
    public void requestReply_success() {

        final ResponseObject response = ResponseObject.builder().id(UUID.randomUUID().toString()).build();
        doReturn(response).when(businessLogic).execute(any());

        final String replyTo = Address.AMQ_RABBITMQ_REPLY_TO + UUID.randomUUID().toString();
        headers.put(AmqpHeaders.REPLY_TO, replyTo);

        handler.handle(
            headers,
            request,
            responseClass,
            () -> businessLogic.execute(request));

        verify(businessLogic).execute(request);

        final Address replyToAddress = new Address(replyTo);
        verify(rabbitTemplate).convertAndSend(
            eq(replyToAddress.getExchangeName()),
            eq(replyToAddress.getRoutingKey()),
            amqpEventCaptor.capture(),
            mppCaptor.capture());
        verifyNoMoreInteractions(rabbitTemplate);

        final AmqpEventObject amqpExpectedEvent = new AmqpEventObject(null, response, null);
        final AmqpEventObject amqpActuaalEvent = amqpEventCaptor.getValue();
        assertEquals(amqpExpectedEvent, amqpActuaalEvent);

        assertCorrelationId();
    }

    @Test
    public void requestReply_error() {

        final Exception exception = new RuntimeException();
        doThrow(exception).when(businessLogic).execute(any());

        final String replyTo = Address.AMQ_RABBITMQ_REPLY_TO + UUID.randomUUID().toString();
        headers.put(AmqpHeaders.REPLY_TO, replyTo);

        handler.handle(
            headers,
            request,
            responseClass,
            () -> businessLogic.execute(request));

        verify(businessLogic).execute(request);

        final Address replyToAddress = new Address(replyTo);
        verify(rabbitTemplate).convertAndSend(
            eq(replyToAddress.getExchangeName()),
            eq(replyToAddress.getRoutingKey()),
            amqpEventCaptor.capture(),
            mppCaptor.capture());
        verifyNoMoreInteractions(rabbitTemplate);

        final AmqpEventObject amqpExpectedEvent = new AmqpEventObject(null, null, ErrorResponse.build(exception));
        final AmqpEventObject amqpActuaalEvent = amqpEventCaptor.getValue();
        assertEquals(amqpExpectedEvent, amqpActuaalEvent);

        assertCorrelationId();
    }

    @Test
    public void requestReply_error_withCorrelationId() {
        hasCorrelationId = true;
        correlationId = nextLong();
        headers.put(AmqpHeaders.CORRELATION_ID, correlationId);

        final Exception exception = new RuntimeException();
        doThrow(exception).when(businessLogic).execute(any());

        final String replyTo = Address.AMQ_RABBITMQ_REPLY_TO + UUID.randomUUID().toString();
        headers.put(AmqpHeaders.REPLY_TO, replyTo);

        handler.handle(
            headers,
            request,
            responseClass,
            () -> businessLogic.execute(request));

        verify(businessLogic).execute(request);

        final Address replyToAddress = new Address(replyTo);
        verify(rabbitTemplate).convertAndSend(
            eq(replyToAddress.getExchangeName()),
            eq(replyToAddress.getRoutingKey()),
            amqpEventCaptor.capture(),
            mppCaptor.capture());
        verifyNoMoreInteractions(rabbitTemplate);

        final AmqpEventObject amqpExpectedEvent = new AmqpEventObject(null, null, ErrorResponse.build(exception));
        final AmqpEventObject amqpActuaalEvent = amqpEventCaptor.getValue();
        assertEquals(amqpExpectedEvent, amqpActuaalEvent);

        assertCorrelationId();
    }

    @Test
    public void asyncEvent_success() {

        final ResponseObject response = ResponseObject.builder().id(UUID.randomUUID().toString()).build();
        doReturn(response).when(businessLogic).execute(any());

        headers.clear();

        handler.handle(
            headers,
            request,
            responseClass,
            () -> businessLogic.execute(request));

        verify(businessLogic).execute(request);

        verify(rabbitTemplate).convertAndSend(
            eq(Messaging.EXCHANGE_EVENTS), eq(SUCCESS_EVENT.getRoutingKey()), amqpEventCaptor.capture());
        verifyNoMoreInteractions(rabbitTemplate);

        final AmqpEventObject amqpExpectedEvent = new AmqpEventObject(request, response, null);
        final AmqpEventObject amqpActuaalEvent = amqpEventCaptor.getValue();
        assertEquals(amqpExpectedEvent, amqpActuaalEvent);
    }

    @Test
    public void asyncEvent_error() {

        final Exception exception = new RuntimeException();
        doThrow(exception).when(businessLogic).execute(any());

        headers.clear();

        handler.handle(
            headers,
            request,
            responseClass,
            () -> businessLogic.execute(request));

        verify(businessLogic).execute(request);

        verify(rabbitTemplate).convertAndSend(
            eq(Messaging.EXCHANGE_EVENTS), eq(ERROR_EVENT.getRoutingKey()), amqpEventCaptor.capture());
        verifyNoMoreInteractions(rabbitTemplate);

        final AmqpEventObject amqpExpectedEvent = new AmqpEventObject(request, null, ErrorResponse.build(exception));
        final AmqpEventObject amqpActuaalEvent = amqpEventCaptor.getValue();
        assertEquals(amqpExpectedEvent, amqpActuaalEvent);
    }

    @Test
    public void asyncEventWithCustomRoute_success() {

        final ResponseObject response = ResponseObject.builder().id(UUID.randomUUID().toString()).build();
        doReturn(response).when(businessLogic).execute(any());

        headers.clear();

        final String exchangeReplyTo = Messaging.EXCHANGE_REPLY_TO + UUID.randomUUID().toString();
        final String routeReplyTo = Messaging.ROUTE_REPLY_TO + UUID.randomUUID().toString();
        headers.put(Messaging.EXCHANGE_REPLY_TO, exchangeReplyTo);
        headers.put(Messaging.ROUTE_REPLY_TO, routeReplyTo);

        handler.handle(headers, request, responseClass, () -> businessLogic.execute(request));

        verify(businessLogic).execute(request);

        verify(rabbitTemplate).convertAndSend(eq(exchangeReplyTo), eq(routeReplyTo), amqpEventCaptor.capture());
        verifyNoMoreInteractions(rabbitTemplate);

        final AmqpEventObject amqpExpectedEvent = new AmqpEventObject(request, response, null);
        final AmqpEventObject amqpActuaalEvent = amqpEventCaptor.getValue();
        assertEquals(amqpExpectedEvent, amqpActuaalEvent);
    }

    @Test(expected = NullPointerException.class)
    public void testNewInstanceException() {
        handler.handle(null, null, null, null);
    }

    private void assertCorrelationId() {

        if (hasCorrelationId) {

            final MessageProperties props = new MessageProperties();

            final Message message = mock(Message.class);
            doReturn(props).when(message).getMessageProperties();

            final MessagePostProcessor mpp = mppCaptor.getValue();
            mpp.postProcessMessage(message);

            assertEquals(String.valueOf(correlationId), props.getCorrelationId());
        }
    }


    static class BusinessLogic {

        ResponseObject execute(final RequestObject request) {
            return null;
        }
    }

    @Builder
    @Data
    static class RequestObject implements Serializable {

        private static final long serialVersionUID = 1624424870908005487L;

        private final String id;
    }

    @Builder
    @Data
    static class ResponseObject implements Serializable {

        private static final long serialVersionUID = 6271081915125629843L;

        private final String id;
    }

    @AllArgsConstructor @NoArgsConstructor
    @Data
    static class AmqpEventObject implements AmqpEvent<RequestObject, ResponseObject> {

        private static final long serialVersionUID = -1637752943793919914L;

        private RequestObject requisicao;
        private ResponseObject resultado;
        private ErrorResponse erro;

        @Override
        public String getSuccessRoute() {
            return SUCCESS_EVENT.getRoutingKey();
        }

        @Override
        public String getErrorRoute() {
            return ERROR_EVENT.getRoutingKey();
        }
    }
}