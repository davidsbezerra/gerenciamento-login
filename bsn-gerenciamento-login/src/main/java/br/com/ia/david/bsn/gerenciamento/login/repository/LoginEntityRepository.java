package br.com.ia.david.bsn.gerenciamento.login.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ia.david.bsn.gerenciamento.login.entity.LoginEntity;

@Repository
public interface LoginEntityRepository extends JpaRepository<LoginEntity, Long> {

    LoginEntity findByLoginAndPassword(final String login, final String password);
    LoginEntity findByLogin(final String login);
    List<LoginEntity> findListByAdmin(final Boolean admin);
}