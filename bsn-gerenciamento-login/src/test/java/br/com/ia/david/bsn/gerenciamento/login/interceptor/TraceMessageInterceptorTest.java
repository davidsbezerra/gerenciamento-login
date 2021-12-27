package br.com.ia.david.bsn.gerenciamento.login.interceptor;

import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.lang.reflect.Method;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;

@RunWith(MockitoJUnitRunner.class)
public class TraceMessageInterceptorTest {

    private static final String TRACE_ID = "X-B3-TraceId";
    private static final String SPAN_ID = "X-B3-SpanId";

    @InjectMocks
    private TraceMessageInterceptor interceptor;

    @Mock
    private Tracer tracer;

    @Mock
    private Span span;

    private Method method;
    private Object target;
    private Message message;
    private Object[] args;
    private TraceContext traceContext;

    @Before
    public void setup() {

        method = getClass().getMethods()[0];
        target = new Object();
        message = MessageBuilder.withBody(UUID.randomUUID().toString().getBytes()).build();
        args = new Object[] {new Object(), message};
        traceContext = TraceContext.newBuilder()
            .traceId(nextLong())
            .spanId(nextLong())
            .build();
    }

    @Test
    public void before_semTraceId_naoDeveGerarTraceId() throws Throwable {

        // given
        message.getMessageProperties().getHeaders().remove(TRACE_ID);
        message.getMessageProperties().getHeaders().remove(SPAN_ID);

        // when
        interceptor.before(method, args, target);

        // then
        assertFalse(message.getMessageProperties().getHeaders().containsKey(TRACE_ID));
        assertFalse(message.getMessageProperties().getHeaders().containsKey(SPAN_ID));
        verifyNoInteractions(tracer);
    }

    @Test
    public void before_comTraceIdNaoNumerico_deveManterTraceIdOriginal() throws Throwable {

        // given
        final Object traceId = UUID.randomUUID().toString();
        message.getMessageProperties().setHeader(TRACE_ID, traceId);

        // when
        interceptor.before(method, args, target);

        // then
        assertTrue(message.getMessageProperties().getHeaders().containsKey(TRACE_ID));
        assertEquals(traceId, message.getMessageProperties().getHeader(TRACE_ID));
        verifyNoInteractions(tracer);
    }

    @Test
    public void before_comTraceIdNumerico_deveSubstituirTraceIdPelaRepresentacaoTextual() throws Throwable {

        // given
        final Long traceId = nextLong();
        message.getMessageProperties().setHeader(TRACE_ID, traceId);

        // when
        interceptor.before(method, args, target);

        // then
        assertTrue(message.getMessageProperties().getHeaders().containsKey(TRACE_ID));
        assertTrue(message.getMessageProperties().getHeaders().get(TRACE_ID) instanceof String);
        assertTrue(message.getMessageProperties().getHeaders().containsKey(SPAN_ID));
        assertTrue(message.getMessageProperties().getHeaders().get(SPAN_ID) instanceof String);
        verifyNoInteractions(tracer);
    }

    @Test
    public void postProcessMessage_semTraceId_comSpan_deveManterTraceIdCorrente() {

        // given
        message.getMessageProperties().getHeaders().remove(TRACE_ID);
        message.getMessageProperties().getHeaders().remove(SPAN_ID);

        doReturn(span).when(tracer).currentSpan();
        doReturn(traceContext).when(span).context();

        // when
        interceptor.postProcessMessage(message);

        // then
        assertTrue(message.getMessageProperties().getHeaders().containsKey(TRACE_ID));
        assertEquals(traceContext.traceId(), message.getMessageProperties().getHeaders().get(TRACE_ID));
        verify(tracer).currentSpan();
        verifyNoMoreInteractions(tracer);
    }

    @Test
    public void postProcessMessage_semTraceId_semSpan_deveGerarTraceId() {

        // given
        message.getMessageProperties().getHeaders().remove(TRACE_ID);
        message.getMessageProperties().getHeaders().remove(SPAN_ID);

        doReturn(null).when(tracer).currentSpan();
        doReturn(span).when(tracer).nextSpan();
        doReturn(span).when(span).name(any());
        doReturn(span).when(span).start();
        doReturn(traceContext).when(span).context();

        // when
        interceptor.postProcessMessage(message);

        // then
        assertTrue(message.getMessageProperties().getHeaders().containsKey(TRACE_ID));
        assertEquals(traceContext.traceId(), message.getMessageProperties().getHeaders().get(TRACE_ID));
        verify(tracer).currentSpan();
        verify(tracer).nextSpan();
        verifyNoMoreInteractions(tracer);
    }

    @Test
    public void postProcessMessage_comTraceIdNaoNumerico_deveManterTraceIdOriginal() {

        // given
        final Object traceId = UUID.randomUUID().toString();
        message.getMessageProperties().setHeader(TRACE_ID, traceId);

        // when
        interceptor.postProcessMessage(message);

        // then
        assertTrue(message.getMessageProperties().getHeaders().containsKey(TRACE_ID));
        assertEquals(traceId, message.getMessageProperties().getHeaders().get(TRACE_ID));
        verifyNoInteractions(tracer);
    }

    @Test
    public void postProcessMessage_comTraceIdNumerico_deveManterTraceIdOriginal() {

        // given
        final Long traceId = nextLong();
        message.getMessageProperties().setHeader(TRACE_ID, traceId);

        // when
        interceptor.postProcessMessage(message);

        // then
        assertTrue(message.getMessageProperties().getHeaders().containsKey(TRACE_ID));
        assertEquals(traceId, message.getMessageProperties().getHeaders().get(TRACE_ID));
        verifyNoInteractions(tracer);
    }
}
