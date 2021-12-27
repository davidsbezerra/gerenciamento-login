package br.com.ia.david.bff.gerenciamento.login.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BffGerenciamentoLoginController implements BffGerenciamentoLoginApi {

    @Override
    @GetMapping("/{parametro}") // FIXME
    public ResponseEntity<HelloWorld> helloWorld(@PathVariable(required = true) String parametro) {
        HelloWorld helloWorld = new HelloWorld(parametro);

        return new ResponseEntity<>(helloWorld, HttpStatus.OK);
    }
}
