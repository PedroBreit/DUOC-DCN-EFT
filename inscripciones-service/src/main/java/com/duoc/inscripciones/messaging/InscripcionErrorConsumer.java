package com.duoc.inscripciones.messaging;

import com.duoc.inscripciones.config.RabbitMqConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InscripcionErrorConsumer {

    private final RabbitTemplate rabbitTemplate;

    public Map<String, Object> consumirUno() {
        Object contenido = rabbitTemplate.receiveAndConvert(
                RabbitMqConfig.INSCRIPCIONES_ERROR_QUEUE
        );

        Map<String, Object> respuesta =
                new LinkedHashMap<>();

        respuesta.put(
                "cola",
                RabbitMqConfig.INSCRIPCIONES_ERROR_QUEUE
        );

        if (contenido == null) {
            respuesta.put("estado", "SIN_MENSAJES");
            respuesta.put(
                    "mensaje",
                    "La cola de errores no contiene mensajes"
            );

            return respuesta;
        }

        respuesta.put("estado", "CONSUMIDO");
        respuesta.put(
                "mensaje",
                "Mensaje consumido desde la cola de errores"
        );
        respuesta.put("contenido", contenido);

        return respuesta;
    }
}