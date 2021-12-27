package br.com.ia.david.bff.gerenciamento.login.web;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HelloWorld implements Serializable { 
    // FIXME Apagar esta classe depois que for criado o primeiro teste de integração.
    // Classe criada somente para ter um teste na criação do projeto.

    private static final long serialVersionUID = 1L;

    private String message;

}
