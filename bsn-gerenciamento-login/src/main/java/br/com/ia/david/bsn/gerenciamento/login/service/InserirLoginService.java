package br.com.ia.david.bsn.gerenciamento.login.service;

import static java.util.Optional.ofNullable;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import br.com.ia.david.bsn.gerenciamento.login.domain.ErrorType;
import br.com.ia.david.bsn.gerenciamento.login.domain.Message;
import br.com.ia.david.bsn.gerenciamento.login.entity.LoginEntity;
import br.com.ia.david.bsn.gerenciamento.login.exception.ClientErrorException;
import br.com.ia.david.bsn.gerenciamento.login.mapper.LoginEntityMapper;
import br.com.ia.david.bsn.gerenciamento.login.mapper.LoginMapper;
import br.com.ia.david.bsn.gerenciamento.login.message.InserirLoginMessage;
import br.com.ia.david.bsn.gerenciamento.login.repository.LoginEntityRepository;
import br.com.ia.david.bsn.gerenciamento.login.response.InserirLoginResponse;
import br.com.ia.david.bsn.gerenciamento.login.service.support.MessageService;
import br.com.ia.david.bsn.gerenciamento.login.validator.InserirLoginValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InserirLoginService {

    private final MessageService messageService;
    private final LoginEntityRepository repository;
    private final InserirLoginValidator validator;

    public InserirLoginResponse inserir(final InserirLoginMessage message) {

        validator.accept(message);

        log.info("Inserindo login {}.", message.getLogin().getLogin());

        ofNullable(repository.findByLogin(message.getLogin().getLogin()))
            .orElseThrow(() -> new ClientErrorException(ErrorType.BUSINESS,
                messageService.get(Message.LOGIN_EXISTENTE)));

        final LoginEntity loginEntity = repository.save(
            Mappers.getMapper(LoginEntityMapper.class)
                .apply(message.getLogin()));

        log.info("Login {} inserido. id {}.", loginEntity.getLogin(), loginEntity.getId());

        return ofNullable(loginEntity)
            .map(entidade -> InserirLoginResponse.builder()
                .login(Mappers.getMapper(LoginMapper.class)
                    .apply(entidade))
                .build())
            .orElseThrow(
                () -> new ClientErrorException(ErrorType.INTERNAL_ERROR, messageService.get(Message.OBJETO_NULO)));
    }
}
