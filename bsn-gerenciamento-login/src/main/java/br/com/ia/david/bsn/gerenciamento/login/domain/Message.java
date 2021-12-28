package br.com.ia.david.bsn.gerenciamento.login.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Message {

    ID_DEVE_SER_NULO("id.deve.ser.nulo"),
    LOGIN_INVALIDO("login.invalido"),
    LOGIN_EXISTENTE("login.existente"),
    NENHUM_RESULTADO_ENCONTRADO("nenhum.resultado.encontrado"),
    OBJETO_NULO("objeto.nulo"),
    SENHA_INVALIDA("senha.invalida"),
    SERVICO_INDISPONIVEL("servico.indisponivel");

    private String message;
}
