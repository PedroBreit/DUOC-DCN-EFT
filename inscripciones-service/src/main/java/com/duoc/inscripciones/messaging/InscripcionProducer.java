package com.duoc.inscripciones.messaging;

import com.duoc.inscripciones.config.RabbitMqConfig;
import com.duoc.inscripciones.dto.InscripcionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InscripcionProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publicar(InscripcionMessage mensaje) {
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.INSCRIPCIONES_EXCHANGE,
                RabbitMqConfig.INSCRIPCIONES_ROUTING_KEY,
                mensaje
        );
    }
}