package br.com.ia.david.bff.gerenciamento.login.interceptor;

import static br.com.ia.david.bff.gerenciamento.login.utils.RandomCollectionUtils.generateList;
import static br.com.ia.david.bff.gerenciamento.login.utils.RandomCollectionUtils.pickRandom;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.springframework.http.ResponseEntity;

import br.com.ia.david.bff.gerenciamento.login.domain.ErrorType;
import br.com.ia.david.bff.gerenciamento.login.exception.ClientErrorException;
import br.com.ia.david.bff.gerenciamento.login.exception.ServerErrorException;
import br.com.ia.david.bff.gerenciamento.login.response.ErrorResponse;

public class ControllerErrorInterceptorTest {

    private final ControllerErrorInterceptor interceptor = new ControllerErrorInterceptor();

    @Test
    public void handleClientErrorException_noDetails() {

        final ErrorType errorType = pickRandom(ErrorType.values());
        final String message = UUID.randomUUID().toString();
        final ClientErrorException exception = new ClientErrorException(errorType, message);

        final ResponseEntity<ErrorResponse> response = interceptor.handleException(exception);

        assertNotNull(response);
        assertEquals(errorType.getHttpStatus(), response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals(errorType, response.getBody().getErrorType());
        assertEquals(message, response.getBody().getMessage());
        assertTrue(response.getBody().getDetails() == null || response.getBody().getDetails().isEmpty());
    }

    @Test
    public void handleClientErrorException_withDetails() {

        final ErrorType errorType = pickRandom(ErrorType.values());
        final String message = UUID.randomUUID().toString();

        final Map<String, String> details = new HashMap<>();
        generateList(() -> UUID.randomUUID().toString(), 1, 10)
            .forEach(detail -> details.put(UUID.randomUUID().toString(), detail));

        final ClientErrorException exception = new ClientErrorException(errorType, message);
        exception.getDetails().putAll(details);

        final ResponseEntity<ErrorResponse> response = interceptor.handleException(exception);

        assertNotNull(response);
        assertEquals(errorType.getHttpStatus(), response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals(errorType, response.getBody().getErrorType());
        assertEquals(message, response.getBody().getMessage());
        assertEquals(details, response.getBody().getDetails());
    }

    @Test
    public void handleServerErrorException_noDetails() {

        final String message = UUID.randomUUID().toString();
        final ServerErrorException exception = new ServerErrorException(message);

        final ResponseEntity<ErrorResponse> response = interceptor.handleException(exception);

        assertNotNull(response);
        assertEquals(ErrorType.INTERNAL_ERROR.getHttpStatus(), response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals(ErrorType.INTERNAL_ERROR, response.getBody().getErrorType());
        assertEquals(message, response.getBody().getMessage());
        assertTrue(response.getBody().getDetails() == null || response.getBody().getDetails().isEmpty());
    }

    @Test
    public void handleServerErrorException_withDetails() {

        final String message = UUID.randomUUID().toString();

        final Map<String, String> details = new HashMap<>();
        generateList(() -> UUID.randomUUID().toString(), 1, 10)
            .forEach(detail -> details.put(UUID.randomUUID().toString(), detail));

        final ServerErrorException exception = new ServerErrorException(message);
        exception.getDetails().putAll(details);

        final ResponseEntity<ErrorResponse> response = interceptor.handleException(exception);

        assertNotNull(response);
        assertEquals(ErrorType.INTERNAL_ERROR.getHttpStatus(), response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals(ErrorType.INTERNAL_ERROR, response.getBody().getErrorType());
        assertEquals(message, response.getBody().getMessage());
        assertEquals(details, response.getBody().getDetails());
    }
}