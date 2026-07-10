package com.duoc.inscripciones.messaging;

import com.duoc.inscripciones.config.RabbitMqConfig;
import com.duoc.inscripciones.dto.InscripcionMessage;
import com.duoc.inscripciones.dto.InscripcionRequest;
import com.duoc.inscripciones.service.InscripcionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InscripcionConsumer {

    private final InscripcionService inscripcionService;

    @RabbitListener(queues = RabbitMqConfig.INSCRIPCIONES_QUEUE)
    public void consumir(InscripcionMessage mensaje) {
        log.info("Mensaje recibido desde RabbitMQ: {}", mensaje);

        try {
            Thread.sleep(10_000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("El procesamiento fue interrumpido", ex);
        }

        if (Boolean.TRUE.equals(mensaje.forzarError())) {
            throw new IllegalStateException("Error forzado para probar la cola de errores");
        }

        InscripcionRequest request = new InscripcionRequest(
                mensaje.idEstudiante(),
                mensaje.nombreEstudiante(),
                mensaje.correoEstudiante(),
                mensaje.idCurso()
        );

        inscripcionService.crear(request);

        log.info(
                "Inscripción procesada para estudiante {} y curso {}",
                mensaje.idEstudiante(),
                mensaje.idCurso()
        );
    }
}