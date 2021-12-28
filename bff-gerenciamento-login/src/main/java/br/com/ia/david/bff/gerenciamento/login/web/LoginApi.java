package br.com.ia.david.bff.gerenciamento.login.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.ia.david.bff.gerenciamento.login.domain.login.Login;
import br.com.ia.david.bff.gerenciamento.login.request.InserirLoginRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "LoginApi")
interface LoginApi {

    @ApiOperation(value = "Insere novo login",
        notes = "Processo de inclusão de novo login")
    @ApiResponses({@ApiResponse(code = 200, message = "Request enviada"),
        @ApiResponse(code = 500, message = "Erro Interno")})
    void inserirLogin(@RequestBody InserirLoginRequest request);

    @ApiOperation(value = "Buscar login no redis",
        notes = "Buscar login no redis")
    @ApiResponses({@ApiResponse(code = 200, message = "Sucesso"),
        @ApiResponse(code = 500, message = "Erro Interno")})
    Login buscarUsuarioInseridoRedis(@PathVariable("id") final Long id);
}
