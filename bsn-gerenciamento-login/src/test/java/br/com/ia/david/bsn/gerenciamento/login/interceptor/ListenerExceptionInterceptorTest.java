package br.com.ia.david.bsn.gerenciamento.login.interceptor;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.amqp.support.converter.DefaultClassMapper.DEFAULT_CLASSID_FIELD_NAME;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.lang.reflect.Method;
import java.util.Base64;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ia.david.bsn.gerenciamento.login.interceptor.ListenerExceptionInterceptor.ArmazenarMensagemMessage;
import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import lombok.SneakyThrows;

@RunWith(MockitoJUnitRunner.class)
public class ListenerExceptionInterceptorTest {

    private ListenerExceptionInterceptor interceptor;

    @Mock
    private ObjectMapper mockMapper;

    @Spy
    private ObjectMapper actualObjectMapper;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private Tracer tracer;

    @Mock
    private Span span;

    private static final String TEST_ERROR = "test_error";
    private byte[] testBytes;

    @Before
    @SneakyThrows
    public void setup(){

        testBytes = randomAlphabetic(30).getBytes();

        when(mockMapper.copy()).thenReturn(actualObjectMapper);

        interceptor = new ListenerExceptionInterceptor(mockMapper);

        setField(interceptor, "rabbitTemplate", rabbitTemplate);
        setField(interceptor, "tracer", tracer);

        when(tracer.currentSpan()).thenReturn(span);
    }

    @Test
    @SneakyThrows
    public void ok_method_args_target_throwable(){

        final Method mockMethod = ReflectionUtils.findMethod(Integer.class, "toString", int.class);
        final Object mockObject = mock(Object.class);
        final Long mockTrace = nextLong();
        final TraceContext traceContext = TraceContext.newBuilder()
            .traceId(mockTrace)
            .spanId(mockTrace)
            .build();
        final String mockExchange = randomAlphabetic(20);
        final String mockQueue = randomAlphabetic(20);
        final String mockRoutingKey = randomAlphabetic(20);
        final String mockTypeId = randomAlphabetic(20);
        final Object[] args = new Object[2];
        final MessageProperties mockProps = new MessageProperties();

        when(span.context()).thenReturn(traceContext);

        mockProps.setReceivedExchange(mockExchange);
        mockProps.setConsumerQueue(mockQueue);
        mockProps.setReceivedRoutingKey(mockRoutingKey);
        mockProps.getHeaders().put(DEFAULT_CLASSID_FIELD_NAME, mockTypeId);

        final Message message = new Message(testBytes, mockProps);

        args[1] = message;

        final Throwable throwable = new Throwable(TEST_ERROR, new Throwable(TEST_ERROR));

        interceptor.afterThrowing(mockMethod, args, mockObject, throwable);

        final ArgumentCaptor<ArmazenarMensagemMessage> armazenarMensagemMessageArgumentCaptor = ArgumentCaptor.forClass(ArmazenarMensagemMessage.class);


        verify(actualObjectMapper).writeValueAsBytes(armazenarMensagemMessageArgumentCaptor.capture());

        final ArmazenarMensagemMessage sentMessage = armazenarMensagemMessageArgumentCaptor.getValue();

        assertEquals(mockProps.getReceivedExchange(), sentMessage.getDestination().getExchange());
        assertEquals(mockProps.getConsumerQueue(), sentMessage.getDestination().getQueue());
        assertEquals(mockProps.getReceivedRoutingKey(), sentMessage.getDestination().getRoutingKey());
        assertEquals((String) mockProps.getHeaders().get(DEFAULT_CLASSID_FIELD_NAME), sentMessage.getDestination().getTypeId());

        assertEquals(sentMessage.getTraceId(), mockTrace);

        assertEquals(String.format("%s: %s", throwable.getMessage(), throwable.getCause().getMessage()), sentMessage.getError().getCause());
        assertNotNull(sentMessage.getError().getTimestamp());

        assertEquals(Base64.getEncoder().encodeToString(message.getBody()),sentMessage.getMessage().getBody());
        assertEquals(mockProps, sentMessage.getMessage().getProperties());

    }

    @Test
    @SneakyThrows
    public void ok_message_throwable(){

        final Long mockTrace = nextLong();
        final TraceContext traceContext = TraceContext.newBuilder()
            .traceId(mockTrace)
            .spanId(mockTrace)
            .build();
        final String mockExchange = randomAlphabetic(20);
        final String mockQueue = randomAlphabetic(20);
        final String mockRoutingKey = randomAlphabetic(20);
        final String mockTypeId = randomAlphabetic(20);
        final MessageProperties mockProps = new MessageProperties();

        when(span.context()).thenReturn(traceContext);

        mockProps.setReceivedExchange(mockExchange);
        mockProps.setConsumerQueue(mockQueue);
        mockProps.setReceivedRoutingKey(mockRoutingKey);
        mockProps.getHeaders().put(DEFAULT_CLASSID_FIELD_NAME, mockTypeId);

        final Message message = new Message(testBytes, mockProps);

        final Throwable throwable = new Throwable(TEST_ERROR, new Throwable(TEST_ERROR));

        interceptor.afterThrowing(message, throwable);

        final ArgumentCaptor<ArmazenarMensagemMessage> armazenarMensagemMessageArgumentCaptor = ArgumentCaptor.forClass(ArmazenarMensagemMessage.class);


        verify(actualObjectMapper).writeValueAsBytes(armazenarMensagemMessageArgumentCaptor.capture());

        final ArmazenarMensagemMessage sentMessage = armazenarMensagemMessageArgumentCaptor.getValue();

        assertEquals(mockProps.getReceivedExchange(), sentMessage.getDestination().getExchange());
        assertEquals(mockProps.getConsumerQueue(), sentMessage.getDestination().getQueue());
        assertEquals(mockProps.getReceivedRoutingKey(), sentMessage.getDestination().getRoutingKey());
        assertEquals((String) mockProps.getHeaders().get(DEFAULT_CLASSID_FIELD_NAME), sentMessage.getDestination().getTypeId());

        assertEquals(sentMessage.getTraceId(), mockTrace);

        assertEquals(String.format("%s: %s", throwable.getMessage(), throwable.getCause().getMessage()), sentMessage.getError().getCause());
        assertNotNull(sentMessage.getError().getTimestamp());

        assertEquals(Base64.getEncoder().encodeToString(message.getBody()),sentMessage.getMessage().getBody());
        assertEquals(mockProps, sentMessage.getMessage().getProperties());
    }

    @Test
    @SneakyThrows
    public void ok_message_throwable_sem_trace_id(){

        final String mockExchange = randomAlphabetic(20);

        final String mockQueue = randomAlphabetic(20);

        final String mockRoutingKey = randomAlphabetic(20);

        final String mockTypeId = randomAlphabetic(20);

        final MessageProperties mockProps = new MessageProperties();

        mockProps.setReceivedExchange(mockExchange);
        mockProps.setConsumerQueue(mockQueue);
        mockProps.setReceivedRoutingKey(mockRoutingKey);
        mockProps.getHeaders().put(DEFAULT_CLASSID_FIELD_NAME, mockTypeId);

        final Message message = new Message(testBytes, mockProps);

        final Throwable throwable = new Throwable(TEST_ERROR, new Throwable(TEST_ERROR));

        interceptor.afterThrowing(message, throwable);

        final ArgumentCaptor<ArmazenarMensagemMessage> armazenarMensagemMessageArgumentCaptor = ArgumentCaptor.forClass(ArmazenarMensagemMessage.class);


        verify(actualObjectMapper).writeValueAsBytes(armazenarMensagemMessageArgumentCaptor.capture());

        final ArmazenarMensagemMessage sentMessage = armazenarMensagemMessageArgumentCaptor.getValue();

        assertEquals(mockProps.getReceivedExchange(), sentMessage.getDestination().getExchange());
        assertEquals(mockProps.getConsumerQueue(), sentMessage.getDestination().getQueue());
        assertEquals(mockProps.getReceivedRoutingKey(), sentMessage.getDestination().getRoutingKey());
        assertEquals((String) mockProps.getHeaders().get(DEFAULT_CLASSID_FIELD_NAME), sentMessage.getDestination().getTypeId());

        assertNull(sentMessage.getTraceId());

        assertEquals(String.format("%s: %s", throwable.getMessage(), throwable.getCause().getMessage()), sentMessage.getError().getCause());
        assertNotNull(sentMessage.getError().getTimestamp());

        assertEquals(Base64.getEncoder().encodeToString(message.getBody()),sentMessage.getMessage().getBody());
        assertEquals(mockProps, sentMessage.getMessage().getProperties());
    }

    @Test
    @SneakyThrows
    public void ok_message_throwable_sem_properties(){

        final Message message = new Message(testBytes, null);

        final Throwable throwable = new Throwable(TEST_ERROR, new Throwable(TEST_ERROR));

        interceptor.afterThrowing(message, throwable);

        final ArgumentCaptor<ArmazenarMensagemMessage> armazenarMensagemMessageArgumentCaptor = ArgumentCaptor.forClass(ArmazenarMensagemMessage.class);


        verify(actualObjectMapper).writeValueAsBytes(armazenarMensagemMessageArgumentCaptor.capture());

        final ArmazenarMensagemMessage sentMessage = armazenarMensagemMessageArgumentCaptor.getValue();

        assertNull(sentMessage.getDestination().getExchange());
        assertNull(sentMessage.getDestination().getQueue());
        assertNull(sentMessage.getDestination().getRoutingKey());
        assertNull(sentMessage.getDestination().getTypeId());

        assertNull(sentMessage.getTraceId());

        assertEquals(String.format("%s: %s", throwable.getMessage(), throwable.getCause().getMessage()), sentMessage.getError().getCause());
        assertNotNull(sentMessage.getError().getTimestamp());

        assertEquals(Base64.getEncoder().encodeToString(message.getBody()),sentMessage.getMessage().getBody());
        assertNull(sentMessage.getMessage().getProperties());
    }
    
}