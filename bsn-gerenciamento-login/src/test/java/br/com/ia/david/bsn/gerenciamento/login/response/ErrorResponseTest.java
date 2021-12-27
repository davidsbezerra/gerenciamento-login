package br.com.ia.david.bsn.gerenciamento.login.response;

import static br.com.ia.david.bsn.gerenciamento.login.utils.RandomCollectionUtils.generateList;
import static br.com.ia.david.bsn.gerenciamento.login.utils.RandomCollectionUtils.pickRandom;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import br.com.ia.david.bsn.gerenciamento.login.domain.ErrorType;
import br.com.ia.david.bsn.gerenciamento.login.exception.ClientErrorException;
import br.com.ia.david.bsn.gerenciamento.login.exception.ServerErrorException;

public class ErrorResponseTest {

    @Test
    public void build_noException() {

        final ErrorResponse errorResponse = ErrorResponse.buildDefault();

        assertNotNull(errorResponse);
        assertEquals(ErrorType.INTERNAL_ERROR, errorResponse.getErrorType());
        assertEquals("Falha inesperada", errorResponse.getMessage());
        assertTrue(errorResponse.getDetails() == null || errorResponse.getDetails().isEmpty());
    }

    @Test
    public void build_clientErrorException_noDetails() {

        final ErrorType errorType = pickRandom(ErrorType.values());
        final String message = UUID.randomUUID().toString();
        final ClientErrorException exception = new ClientErrorException(errorType, message);

        final ErrorResponse errorResponse = ErrorResponse.build(exception);
        assertNotNull(errorResponse);
        assertEquals(errorType, errorResponse.getErrorType());
        assertEquals(message, errorResponse.getMessage());
        assertTrue(errorResponse.getDetails() == null || errorResponse.getDetails().isEmpty());
    }

    @Test
    public void build_clientErrorException_withDetails() {

        final ErrorType errorType = pickRandom(ErrorType.values());
        final String message = UUID.randomUUID().toString();

        final Map<String, String> details = new HashMap<>();
        generateList(() -> UUID.randomUUID().toString(), 1, 10)
            .forEach(detail -> details.put(UUID.randomUUID().toString(), detail));

        final ClientErrorException exception = new ClientErrorException(errorType, message);
        exception.getDetails().putAll(details);

        final ErrorResponse errorResponse = ErrorResponse.build(exception);
        assertNotNull(errorResponse);
        assertEquals(errorType, errorResponse.getErrorType());
        assertEquals(message, errorResponse.getMessage());
        assertEquals(details, errorResponse.getDetails());
    }

    @Test
    public void build_serverErrorException_noDetails() {

        final String message = UUID.randomUUID().toString();
        final ServerErrorException exception = new ServerErrorException(message);

        final ErrorResponse errorResponse = ErrorResponse.build(exception);
        assertNotNull(errorResponse);
        assertEquals(ErrorType.INTERNAL_ERROR, errorResponse.getErrorType());
        assertEquals(message, errorResponse.getMessage());
        assertTrue(errorResponse.getDetails() == null || errorResponse.getDetails().isEmpty());
    }

    @Test
    public void build_serverErrorException_withDetails() {

        final String message = UUID.randomUUID().toString();

        final Map<String, String> details = new HashMap<>();
        generateList(() -> UUID.randomUUID().toString(), 1, 10)
            .forEach(detail -> details.put(UUID.randomUUID().toString(), detail));

        final ServerErrorException exception = new ServerErrorException(message, new Exception());
        exception.getDetails().putAll(details);

        final ErrorResponse errorResponse = ErrorResponse.build(Exception.class.cast(exception));
        assertNotNull(errorResponse);
        assertEquals(ErrorType.INTERNAL_ERROR, errorResponse.getErrorType());
        assertEquals(message, errorResponse.getMessage());
        assertEquals(details, errorResponse.getDetails());
    }

    @Test
    public void build_generalException() {

        final Exception[] exceptions = new Exception[] {
            new NullPointerException(), new IllegalArgumentException(), new IllegalStateException(),
            new RuntimeException(), new Exception() };

        final Exception exception = pickRandom(exceptions);

        final ErrorResponse errorResponse = ErrorResponse.build(exception);
        assertEquals(ErrorType.INTERNAL_ERROR, errorResponse.getErrorType());
        assertEquals("Falha inesperada", errorResponse.getMessage());
        assertTrue(errorResponse.getDetails() == null || errorResponse.getDetails().isEmpty());
    }
}