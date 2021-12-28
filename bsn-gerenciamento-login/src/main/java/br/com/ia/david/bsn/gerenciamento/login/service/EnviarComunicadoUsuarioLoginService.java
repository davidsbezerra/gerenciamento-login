package br.com.ia.david.bsn.gerenciamento.login.service;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.com.ia.david.bsn.gerenciamento.login.domain.ErrorType;
import br.com.ia.david.bsn.gerenciamento.login.domain.Message;
import br.com.ia.david.bsn.gerenciamento.login.entity.LoginEntity;
import br.com.ia.david.bsn.gerenciamento.login.exception.ClientErrorException;
import br.com.ia.david.bsn.gerenciamento.login.message.EnviarComunicadoMessage;
import br.com.ia.david.bsn.gerenciamento.login.repository.LoginEntityRepository;
import br.com.ia.david.bsn.gerenciamento.login.response.EnviarComunicadoResponse;
import br.com.ia.david.bsn.gerenciamento.login.service.support.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnviarComunicadoUsuarioLoginService {

    private final MessageService messageService;
    private final LoginEntityRepository repository;
    private final JavaMailSender mailSender;

    public EnviarComunicadoResponse enviar(final EnviarComunicadoMessage message) {

        if (isNull(message) || isBlank(message.getMensagem())) {
            throw new ClientErrorException(ErrorType.BUSINESS,
                messageService.get(Message.NENHUM_RESULTADO_ENCONTRADO));
        }

        log.info("Enviando email");

        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("sistema@sistema.com.br");

        final List<String> destino = ofNullable(message.getLogin())

            .map(l -> repository.findByLogin(l))
            .map(LoginEntity::getEmail)
            .map(Arrays::asList)
            .orElse(
                repository.findListByAdmin(Boolean.TRUE)
                    .stream()
                    .map(LoginEntity::getEmail)
                    .collect(Collectors.toList())
            );

        simpleMailMessage.setTo(StringUtils.join(destino, ";"));

        try {
            mailSender.send(simpleMailMessage);
            return EnviarComunicadoResponse.builder()
                .sucesso(true)
                .build();

        } catch (Exception e) {
            throw new ClientErrorException(ErrorType.INTERNAL_ERROR,
                messageService.get(Message.NENHUM_RESULTADO_ENCONTRADO), e);
        }
    }
}
