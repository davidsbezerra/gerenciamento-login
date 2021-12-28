package br.com.ia.david.bff.gerenciamento.login.web;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.ia.david.bff.gerenciamento.login.domain.login.Login;
import br.com.ia.david.bff.gerenciamento.login.request.EfetuarLoginRequest;
import br.com.ia.david.bff.gerenciamento.login.request.InserirLoginRequest;
import br.com.ia.david.bff.gerenciamento.login.service.BuscarUsuarioLogadoNoRedisService;
import br.com.ia.david.bff.gerenciamento.login.service.EfetuarLoginService;
import br.com.ia.david.bff.gerenciamento.login.service.InserirLoginService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController implements LoginApi {

    private final InserirLoginService inserirLoginService;
    private final EfetuarLoginService efetuarLoginService;
    private final BuscarUsuarioLogadoNoRedisService buscarUsuarioLogadoNoRedisService;

    @Override
    @PostMapping
    @ResponseStatus(ACCEPTED)
    public void inserirLogin(@RequestBody final InserirLoginRequest request) {
        inserirLoginService.inserir(request);
    }

    @Override
    @GetMapping()
    @ResponseStatus(ACCEPTED)
    public void efetuarLogin(@RequestBody final EfetuarLoginRequest request) {
        efetuarLoginService.efetuar(request);
    }

    @Override
    @GetMapping("/{id}/redis")
    @ResponseStatus(OK)
    public Login buscarUsuarioInseridoRedis(@PathVariable("id") final Long id) {

        return buscarUsuarioLogadoNoRedisService.buscar(id);
    }
}
