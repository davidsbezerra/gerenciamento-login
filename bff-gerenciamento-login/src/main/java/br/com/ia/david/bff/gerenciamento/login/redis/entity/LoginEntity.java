package br.com.ia.david.bff.gerenciamento.login.redis.entity;


import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@RedisHash(value = "LOGIN_ENTITY")
@EqualsAndHashCode
public class LoginEntity {

    @TimeToLive
    private Long ttl;

    @Id
    @Indexed
    private Long id;

    private String name;

    private String login;

    private String password;

    @Override
    public String toString() {
        return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
            .append("id", ofNullable(getId()).map(id -> id + "-" + login).orElse(null))
            .toString();
    }
}
