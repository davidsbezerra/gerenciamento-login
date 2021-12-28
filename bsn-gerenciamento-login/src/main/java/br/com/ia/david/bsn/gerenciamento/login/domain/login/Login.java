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

    private Long id;
    private String name;
    private String login;
    private String password;
    private OffsetDateTime createdDate;
    private OffsetDateTime updatedDate;
    private String email;
    private Boolean admin;

}
