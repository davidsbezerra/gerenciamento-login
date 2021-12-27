package br.com.ia.david.bsn.gerenciamento.login.interceptor;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextBytes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import lombok.SneakyThrows;

@RunWith(MockitoJUnitRunner.class)
public class HeaderMessageInterceptorTest {

    @InjectMocks
    private HeaderMessageInterceptor interceptor;

    @Test
    @SneakyThrows
    public void test_before(){

        Method mockMethod = ReflectionUtils.findMethod(Integer.class, "toString", int.class);

        Object mockObject = mock(Object.class);

        Object[] args = new Object[2];

        MessageProperties mockProps = new MessageProperties();
        mockProps.getHeaders().put("TEST_KEY", "TEST_VALUE");

        Message message = new Message(nextBytes(30), mockProps);

        args[1] = message;
        ThreadLocal<Map<String, Object>> threadLocal = (ThreadLocal<Map<String, Object>>) ReflectionTestUtils.getField(interceptor, "headers");

        interceptor.before(mockMethod, args, mockObject);

        assertEquals(threadLocal.get(), message.getMessageProperties().getHeaders());

    }

    @Test
    public void ok_post_process_thread_existente(){

        ThreadLocal<Map<String, Object>> mockThread = new ThreadLocal<>();

        final String presetKey = "PRESET_KEY";

        final String rabbitKey = "__RABBIT_KEY";

        final String messageKey = "MESSAGE_KEY";

        Map<String, Object> headers = new HashMap<>();

        headers.put(presetKey, randomAlphabetic(5));

        headers.put(rabbitKey, randomAlphabetic(5));

        mockThread.set(headers);

        ReflectionTestUtils.setField(interceptor, "headers", mockThread);

        MessageProperties mockProps = new MessageProperties();
        mockProps.getHeaders().put(messageKey, randomAlphabetic(5));

        Message message = new Message(nextBytes(30), mockProps);

        Message actual = interceptor.postProcessMessage(message);

        assertEquals(message, actual);
        assertTrue(actual.getMessageProperties().getHeaders().containsKey(presetKey));
        assertTrue(actual.getMessageProperties().getHeaders().containsKey(messageKey));
        assertFalse(actual.getMessageProperties().getHeaders().containsKey(rabbitKey));
    }

    @Test
    public void post_process_thread_null(){

        ReflectionTestUtils.setField(interceptor, "headers", new ThreadLocal<Map<String, Object>>());

        final String messageKey = "MESSAGE_KEY";

        MessageProperties mockProps = new MessageProperties();
        mockProps.getHeaders().put(messageKey, randomAlphabetic(5));

        Message message = new Message(nextBytes(30), mockProps);

        Message actual = interceptor.postProcessMessage(message);

        assertEquals(message, actual);
        assertTrue(actual.getMessageProperties().getHeaders().containsKey(messageKey));
    }

}