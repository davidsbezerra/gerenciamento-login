package br.com.ia.david.bff.gerenciamento.login.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Anotação para RabbitTemplate de eventos.
 */
@Documented
@Inherited
@Qualifier("bff-gerenciamento-login-event-rabbit-template")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface EventTemplate {

}
