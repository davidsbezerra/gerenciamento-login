package br.com.ia.david.bff.gerenciamento.login.web;

import static org.springframework.http.HttpStatus.ACCEPTED;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.ia.david.bff.gerenciamento.login.service.EnviarComunicadoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/comunicacao")
@RequiredArgsConstructor
public class ComunicacaoController implements ComunicacaoApi {

    private final EnviarComunicadoService enviarComunicadoService;

    @PostMapping("/email")
    @ResponseStatus(ACCEPTED)
    public void enviarComunicado(@RequestParam final String mensagem,
        @RequestParam(required = false) final String usuario) {
        enviarComunicadoService.enviar(mensagem, usuario);
    }
}
