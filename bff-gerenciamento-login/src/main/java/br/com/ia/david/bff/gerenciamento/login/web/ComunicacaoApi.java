package br.com.ia.david.bff.gerenciamento.login.web;

import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "ComunicacaoApi")
interface ComunicacaoApi {

    @ApiOperation(value = "Enviar comunicado",
        notes = "Envia mensagem para o email dos usuários")
    @ApiResponses({@ApiResponse(code = 202, message = "Request enviada"),
        @ApiResponse(code = 500, message = "Erro Interno")})
    void enviarComunicado(@RequestParam(required = false) final String mensagem,
        @RequestParam(required = false) final String usuario);

}
