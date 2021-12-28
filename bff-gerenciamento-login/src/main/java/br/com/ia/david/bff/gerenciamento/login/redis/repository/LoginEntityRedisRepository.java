package br.com.ia.david.bff.gerenciamento.login.redis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.ia.david.bff.gerenciamento.login.redis.entity.LoginEntity;

@Repository
public interface LoginEntityRedisRepository extends CrudRepository<LoginEntity, Long> {

}
