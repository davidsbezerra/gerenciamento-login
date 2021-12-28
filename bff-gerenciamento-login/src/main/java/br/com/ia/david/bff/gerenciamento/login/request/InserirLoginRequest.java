package br.com.ia.david.bff.gerenciamento.login.request;

import java.io.Serializable;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class InserirLoginRequest implements Serializable {

    private static final long serialVersionUID = 7525445670659515109L;

    private final String name;
    private final String login;
    private final String password;
    private final OffsetDateTime createdDate;
    private final OffsetDateTime updatedDate;
    private final String email;
    private final Boolean admin;

}
