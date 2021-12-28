package br.com.ia.david.bff.gerenciamento.login.web;

import static org.springframework.http.HttpStatus.ACCEPTED;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.ia.david.bff.gerenciamento.login.request.InserirLoginRequest;
import br.com.ia.david.bff.gerenciamento.login.service.InserirLoginService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController implements LoginApi {

    private final InserirLoginService inserirLoginService;

    @Override
    @PostMapping
    @ResponseStatus(ACCEPTED)
    public void inserirLogin(@RequestBody InserirLoginRequest request) {
        inserirLoginService.inserir(request);
    }

}
