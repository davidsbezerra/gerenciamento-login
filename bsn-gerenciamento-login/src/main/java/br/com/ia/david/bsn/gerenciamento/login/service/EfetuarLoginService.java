package br.com.ia.david.bsn.gerenciamento.login.service;

import static java.util.Optional.ofNullable;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import br.com.ia.david.bsn.gerenciamento.login.domain.ErrorType;
import br.com.ia.david.bsn.gerenciamento.login.domain.Message;
import br.com.ia.david.bsn.gerenciamento.login.exception.ClientErrorException;
import br.com.ia.david.bsn.gerenciamento.login.mapper.LoginMapper;
import br.com.ia.david.bsn.gerenciamento.login.message.EfetuarLoginMessage;
import br.com.ia.david.bsn.gerenciamento.login.repository.LoginEntityRepository;
import br.com.ia.david.bsn.gerenciamento.login.response.EfetuarLoginResponse;
import br.com.ia.david.bsn.gerenciamento.login.service.support.MessageService;
import br.com.ia.david.bsn.gerenciamento.login.validator.AtualizarLoginValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EfetuarLoginService {

    private final MessageService messageService;
    private final LoginEntityRepository repository;
    private final AtualizarLoginValidator validator;

    public EfetuarLoginResponse efetuar(final EfetuarLoginMessage message) {

        //validator.accept(message);

        log.info("Efetuando login {}", message.getLogin().getLogin());

        return ofNullable(
            repository.findByLoginAndPassword(message.getLogin().getLogin(), message.getLogin().getPassword()))
            .map(e -> EfetuarLoginResponse.builder()
                .login(Mappers.getMapper(LoginMapper.class).apply(e))
                .build())
            .orElseThrow(() -> new ClientErrorException(ErrorType.NOT_FOUND,
                messageService.get(Message.NENHUM_RESULTADO_ENCONTRADO)));
    }
}