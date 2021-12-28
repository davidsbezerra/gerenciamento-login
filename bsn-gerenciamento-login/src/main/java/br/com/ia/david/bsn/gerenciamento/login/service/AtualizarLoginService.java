package br.com.ia.david.bsn.gerenciamento.login.service;

import static java.util.Optional.ofNullable;

import org.apache.commons.lang3.BooleanUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import br.com.ia.david.bsn.gerenciamento.login.domain.ErrorType;
import br.com.ia.david.bsn.gerenciamento.login.domain.Message;
import br.com.ia.david.bsn.gerenciamento.login.exception.ClientErrorException;
import br.com.ia.david.bsn.gerenciamento.login.mapper.LoginEntityMapper;
import br.com.ia.david.bsn.gerenciamento.login.mapper.LoginMapper;
import br.com.ia.david.bsn.gerenciamento.login.message.AtualizarLoginMessage;
import br.com.ia.david.bsn.gerenciamento.login.repository.LoginEntityRepository;
import br.com.ia.david.bsn.gerenciamento.login.response.AtualizarLoginResponse;
import br.com.ia.david.bsn.gerenciamento.login.service.support.MessageService;
import br.com.ia.david.bsn.gerenciamento.login.validator.AtualizarLoginValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AtualizarLoginService {

    private final MessageService messageService;
    private final LoginEntityRepository repository;
    private final AtualizarLoginValidator validator;

    public AtualizarLoginResponse atualizar(final AtualizarLoginMessage message) {

        validator.accept(message);

        log.info("Atualizando login {} id {}.", message.getLogin().getLogin(), message.getLogin().getId());

        return ofNullable(repository.existsById(message.getLogin().getId()))
            .filter(BooleanUtils::isTrue)
            .map(e ->
                repository.save(
                    Mappers.getMapper(LoginEntityMapper.class)
                        .apply(message.getLogin())))
            .map(e -> AtualizarLoginResponse.builder()
                .login(Mappers.getMapper(LoginMapper.class)
                    .apply(e))
                .build())
            .orElseThrow(() -> new ClientErrorException(ErrorType.NOT_FOUND,
                messageService.get(Message.NENHUM_RESULTADO_ENCONTRADO)));
    }
}