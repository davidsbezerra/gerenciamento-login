package br.com.ia.david.bff.gerenciamento.login.service;


import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.ia.david.bff.gerenciamento.login.domain.ErrorType;
import br.com.ia.david.bff.gerenciamento.login.domain.Message;
import br.com.ia.david.bff.gerenciamento.login.domain.login.Login;
import br.com.ia.david.bff.gerenciamento.login.exception.ClientErrorException;
import br.com.ia.david.bff.gerenciamento.login.redis.entity.LoginEntity;
import br.com.ia.david.bff.gerenciamento.login.redis.repository.LoginEntityRedisRepository;
import br.com.ia.david.bff.gerenciamento.login.service.support.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BuscarUsuarioLogadoNoRedisService {

    private final LoginEntityRedisRepository repository;

    private final MessageService messageService;

    public Login buscar(final Long id) {

        final Optional<LoginEntity> redisEntity = repository.findById(id);

        return redisEntity.map(re -> Login.builder()
                .login(re.getLogin())
                .id(re.getId())
                .password(re.getPassword())
                .name(re.getName())
                .build())
            .orElseThrow(() -> new ClientErrorException(ErrorType.NOT_FOUND,
                messageService.get(Message.NENHUM_RESULTADO_ENCONTRADO)));
    }
}
