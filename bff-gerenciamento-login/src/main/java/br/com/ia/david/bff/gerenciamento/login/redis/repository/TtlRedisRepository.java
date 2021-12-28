package br.com.ia.david.bff.gerenciamento.login.redis.repository;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import br.com.ia.david.bff.gerenciamento.login.redis.RedisTemplateFactory;

/**
 * Implementação de repositório Redis que aplica Time to Live (ttl).
 *
 * @param <K> tipo das chaves a serem utilizadas, internamente será convertido para String
 * @param <V> tipo dos valores a serem armazenados
 */
public abstract class TtlRedisRepository<K, V extends Serializable> {

    private static final Long DEFAULT_EXPIRE = -1L;
    private static final String APP_KEY = "BSN_LOJA_CAPTACAO";

    private RedisTemplateFactory redisTemplateFactory;
    private RedisTemplate<String, V> redisTemplate;

    @Autowired
    public final void setRedisTemplateFactory(final RedisTemplateFactory redisTemplateFactory) {
        this.redisTemplateFactory = redisTemplateFactory;
    }

    /**
     * Adiciona um valor. Se a chave já possuir um valor, será atualizado.
     *
     * @param chave que será composta e usada como chave do redis
     * @param valor que será armazenado
     */
    public void add(final K chave, final V valor) {
        requireNonNull(chave);
        requireNonNull(valor);

        Long ttl = getTtl();
        if (Long.signum(ttl) > 0) {
            getOperations(chave).set(valor, ttl, TimeUnit.SECONDS);
        } else {
            getOperations(chave).set(valor);
        }
    }

    /**
     * Deleta o valor associado à chave
     */
    public void delete(final K chave) {
        requireNonNull(chave);

        String chaveRedis = comporChaveRedis(chave);
        getRedisTemplate().delete(chaveRedis);
    }

    /**
     * Retorna o valor associado à chave.
     */
    public V get(final K chave) {
        requireNonNull(chave);

        return getOperations(chave).get();
    }

    /**
     * Retorna o tempo de expiração (em <strong>segundos</strong>) para as chaves deste repositório.<br> Valores não
     * positivos não terão tempo de expiração.<br> Por padrão não existe tempo de expiração.
     */
    Long getTtl() {
        return DEFAULT_EXPIRE;
    }

    /**
     * Retorna o prefixo que será utilizado na criação da chave do hash.
     */
    abstract String getPrefix();

    /**
     * Retorna o tipo dos valores a serem armazenados. Necessário para uma correta (de)serialização.
     */
    abstract Class<V> getValueType();

    /**
     * Converte chave para string. Necessário para compor a chave para o redis e facilita a visualização no console,
     * caso necessário.
     */
    abstract String converterParaChaveString(K chave);

    private RedisTemplate<String, V> getRedisTemplate() {
        if (isNull(redisTemplate)) {
            redisTemplate = redisTemplateFactory.criar(getValueType());
        }
        return redisTemplate;
    }

    private BoundValueOperations<String, V> getOperations(final K chave) {
        final String chaveRedis = comporChaveRedis(chave);
        return getRedisTemplate().boundValueOps(chaveRedis);
    }

    private String comporChaveRedis(final K chave) {
        final String chaveString = converterParaChaveString(chave);
        return String.format("%s:%s:%s", APP_KEY, getPrefix(), chaveString);
    }

}
