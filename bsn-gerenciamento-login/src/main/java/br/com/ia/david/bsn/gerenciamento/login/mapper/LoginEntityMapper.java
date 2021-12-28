package br.com.ia.david.bsn.gerenciamento.login.mapper;

import java.util.function.Function;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import br.com.ia.david.bsn.gerenciamento.login.domain.login.Login;
import br.com.ia.david.bsn.gerenciamento.login.entity.LoginEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface LoginEntityMapper extends Function<Login, LoginEntity> {

    LoginEntity apply(Login login);
}
