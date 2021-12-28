package br.com.ia.david.bff.gerenciamento.login.redis;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Component
public class RedisTemplateFactory {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    public <T extends Serializable> RedisTemplate<String, T> criar(Class<T> type) {
        final RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        template.setValueSerializer(criaValueSerializer(type));
        template.setHashValueSerializer(new StringRedisSerializer());

        template.setConnectionFactory(redisConnectionFactory);
        template.afterPropertiesSet();

        return template;
    }

    private <T extends Serializable> Jackson2JsonRedisSerializer<T> criaValueSerializer(final Class<T> type) {
        final Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(type);
        final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .serializationInclusion(Include.NON_NULL)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        serializer.setObjectMapper(objectMapper);

        return serializer;
    }

}
