package com.duoc.inscripciones.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitMqConfig {

    public static final String INSCRIPCIONES_EXCHANGE = "inscripciones.exchange";
    public static final String INSCRIPCIONES_QUEUE = "inscripciones.queue";
    public static final String INSCRIPCIONES_ROUTING_KEY = "inscripciones.nueva";

    public static final String INSCRIPCIONES_DLX = "inscripciones.dlx";
    public static final String INSCRIPCIONES_ERROR_QUEUE = "inscripciones.error.queue";
    public static final String INSCRIPCIONES_ERROR_ROUTING_KEY = "inscripciones.error";

    @Bean
    public DirectExchange inscripcionesExchange() {
        return new DirectExchange(INSCRIPCIONES_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange inscripcionesDeadLetterExchange() {
        return new DirectExchange(INSCRIPCIONES_DLX, true, false);
    }

    @Bean
    public Queue inscripcionesQueue() {
        return new Queue(
                INSCRIPCIONES_QUEUE,
                true,
                false,
                false,
                Map.of(
                        "x-dead-letter-exchange", INSCRIPCIONES_DLX,
                        "x-dead-letter-routing-key", INSCRIPCIONES_ERROR_ROUTING_KEY
                )
        );
    }

    @Bean
    public Queue inscripcionesErrorQueue() {
        return new Queue(INSCRIPCIONES_ERROR_QUEUE, true);
    }

    @Bean
    public Binding inscripcionesBinding() {
        return BindingBuilder
                .bind(inscripcionesQueue())
                .to(inscripcionesExchange())
                .with(INSCRIPCIONES_ROUTING_KEY);
    }

    @Bean
    public Binding inscripcionesErrorBinding() {
        return BindingBuilder
                .bind(inscripcionesErrorQueue())
                .to(inscripcionesDeadLetterExchange())
                .with(INSCRIPCIONES_ERROR_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}