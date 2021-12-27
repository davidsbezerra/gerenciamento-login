package br.com.ia.david.bsn.gerenciamento.login.exception;

import br.com.ia.david.bsn.gerenciamento.login.domain.ErrorType;

public class ClientErrorException extends AbstractErrorException {

    private static final long serialVersionUID = 1486297407038116871L;

    private final ErrorType errorType;

    public ClientErrorException(ErrorType errorType, String msg) {
        super(msg);
        this.errorType = errorType;
    }

    @Override
    public ErrorType getErrorType() {
        return errorType;
    }

}
