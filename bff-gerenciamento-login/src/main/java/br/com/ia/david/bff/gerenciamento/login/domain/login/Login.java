package br.com.ia.david.bff.gerenciamento.login.domain.login;

import java.io.Serializable;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Login implements Serializable {

    private static final long serialVersionUID = 6820168476303851791L;

    private final Long id;
    private final String name;
    private final String login;
    private final String password;
    private final OffsetDateTime createdDate;
    private final OffsetDateTime updatedDate;
    private final String email;
    private final Boolean admin;

}
