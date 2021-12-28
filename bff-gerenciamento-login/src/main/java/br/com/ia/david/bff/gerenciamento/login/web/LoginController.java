package br.com.ia.david.bff.gerenciamento.login.web;

import static org.springframework.http.HttpStatus.ACCEPTED;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.ia.david.bff.gerenciamento.login.domain.login.Login;
import br.com.ia.david.bff.gerenciamento.login.redis.entity.LoginEntity;
import br.com.ia.david.bff.gerenciamento.login.redis.repository.LoginEntityRedisRepository;
import br.com.ia.david.bff.gerenciamento.login.request.InserirLoginRequest;
import br.com.ia.david.bff.gerenciamento.login.service.InserirLoginService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController implements LoginApi {

    private final InserirLoginService inserirLoginService;
    private final LoginEntityRedisRepository repository;

    @Override
    @PostMapping
    @ResponseStatus(ACCEPTED)
    public void inserirLogin(@RequestBody final InserirLoginRequest request) {
        inserirLoginService.inserir(request);
    }

    @Override
    @GetMapping("/{id}/redis")
    @ResponseStatus(ACCEPTED)
    public Login buscarUsuarioInseridoRedis(@PathVariable("id") final Long id) {
        final Optional<LoginEntity> redisEntity = repository.findById(id);

        return redisEntity.map(re -> Login.builder()
                .login(re.getLogin())
                .id(re.getId())
                .password(re.getPassword())
                .name(re.getName())
                .build())
            .orElse(null);
    }
}
