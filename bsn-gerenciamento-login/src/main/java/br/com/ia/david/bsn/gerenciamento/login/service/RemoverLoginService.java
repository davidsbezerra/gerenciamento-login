package br.com.ia.david.bsn.gerenciamento.login.service;

import static br.com.ia.david.bsn.gerenciamento.login.domain.ErrorType.VALIDATION;
import static br.com.ia.david.bsn.gerenciamento.login.domain.Message.OBJETO_NULO;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import br.com.ia.david.bsn.gerenciamento.login.domain.ErrorType;
import br.com.ia.david.bsn.gerenciamento.login.domain.Message;
import br.com.ia.david.bsn.gerenciamento.login.exception.ClientErrorException;
import br.com.ia.david.bsn.gerenciamento.login.message.RemoverLoginMessage;
import br.com.ia.david.bsn.gerenciamento.login.repository.LoginEntityRepository;
import br.com.ia.david.bsn.gerenciamento.login.response.RemoverLoginResponse;
import br.com.ia.david.bsn.gerenciamento.login.service.support.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RemoverLoginService {

    private final MessageService messageService;
    private final LoginEntityRepository repository;

    public RemoverLoginResponse remover(final RemoverLoginMessage message) {

        if (isNull(message) || isNull(message.getId())) {
            log.warn("Request inválida.");
            throw new ClientErrorException(VALIDATION, messageService.get(OBJETO_NULO));
        }

        log.info("Removendo login id {}.", message.getId());

        ofNullable(repository.existsById(message.getId()))
            .filter(BooleanUtils::isTrue)
            .orElseThrow(() -> new ClientErrorException(ErrorType.NOT_FOUND,
                messageService.get(Message.NENHUM_RESULTADO_ENCONTRADO)));

        repository.deleteById(message.getId());

        return RemoverLoginResponse.builder()
            .sucesso(true)
            .build();
    }
}
