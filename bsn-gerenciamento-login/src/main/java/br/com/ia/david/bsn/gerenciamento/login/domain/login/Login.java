package br.com.ia.david.bsn.gerenciamento.login.domain.login;

import java.io.Serializable;
import java.time.OffsetDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Login implements Serializable {

    private static final long serialVersionUID = -8236399378645617426L;

    private final Long id;
    private final String name;
    private final String login;
    private final String password;
    private final OffsetDateTime createdDate;
    private final OffsetDateTime updatedDate;
    private final String email;
    private final Boolean admin;

}
