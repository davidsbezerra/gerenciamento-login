package br.com.ia.david.bff.gerenciamento.login.validator;

import static br.com.ia.david.bff.gerenciamento.login.domain.ErrorType.VALIDATION;
import static java.util.Objects.isNull;

import java.util.function.Consumer;

import org.springframework.stereotype.Component;

import br.com.ia.david.bff.gerenciamento.login.domain.Message;
import br.com.ia.david.bff.gerenciamento.login.exception.ClientErrorException;
import br.com.ia.david.bff.gerenciamento.login.request.InserirLoginRequest;
import br.com.ia.david.bff.gerenciamento.login.service.support.MessageService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor

public class InserirLoginValidator implements Consumer<InserirLoginRequest> {

    private final MessageService messageService;

    @Override
    public void accept(final InserirLoginRequest request) {

        if (isNull(request)) {
            throw new ClientErrorException(VALIDATION, messageService.get(Message.OBJETO_NULO));
        }

        if (isNull(request.getPassword())) {
            throw new ClientErrorException(VALIDATION, messageService.get(Message.SENHA_INVALIDA));
        }

        if (isNull(request.getLogin())) {
            throw new ClientErrorException(VALIDATION, messageService.get(Message.LOGIN_INVALIDO));
        }

        if (isNull(request.getName())) {
            throw new ClientErrorException(VALIDATION, messageService.get(Message.ATRIBUTO_INVALIDO));
        }
    }
}
