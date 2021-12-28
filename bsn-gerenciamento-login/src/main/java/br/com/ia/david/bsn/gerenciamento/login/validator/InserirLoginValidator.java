package br.com.ia.david.bsn.gerenciamento.login.validator;

import static br.com.ia.david.bsn.gerenciamento.login.domain.ErrorType.VALIDATION;
import static br.com.ia.david.bsn.gerenciamento.login.domain.Message.LOGIN_INVALIDO;
import static br.com.ia.david.bsn.gerenciamento.login.domain.Message.OBJETO_NULO;
import static br.com.ia.david.bsn.gerenciamento.login.domain.Message.SENHA_INVALIDA;
import static java.util.Objects.isNull;

import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import br.com.ia.david.bsn.gerenciamento.login.exception.ClientErrorException;
import br.com.ia.david.bsn.gerenciamento.login.message.InserirLoginMessage;
import br.com.ia.david.bsn.gerenciamento.login.service.support.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class InserirLoginValidator implements Consumer<InserirLoginMessage> {

    private final MessageService messageService;

    @Override
    public void accept(final InserirLoginMessage message) {

        if (isNull(message)) {
            log.warn("Objeto nulo.");
            throw new ClientErrorException(VALIDATION, messageService.get(OBJETO_NULO));
        }

        if (isNull(message.getLogin())) {
            log.warn("Objeto nulo.");
            throw new ClientErrorException(VALIDATION, messageService.get(OBJETO_NULO));
        }

        if (StringUtils.isBlank(message.getLogin().getLogin())) {
            log.warn("Login nulo.");
            throw new ClientErrorException(VALIDATION, messageService.get(LOGIN_INVALIDO));
        }

        if (StringUtils.isBlank(message.getLogin().getPassword())) {
            log.warn("Senha não deve ser nula.");
            throw new ClientErrorException(VALIDATION, messageService.get(SENHA_INVALIDA));
        }

    }
}
