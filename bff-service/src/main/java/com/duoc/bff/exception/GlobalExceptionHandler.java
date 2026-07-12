package com.duoc.bff.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<Map<String, Object>> manejarErrorRemoto(
            RestClientResponseException ex
    ) {
        Map<String, Object> respuesta = new LinkedHashMap<>();

        respuesta.put(
                "timestamp",
                LocalDateTime.now()
        );
        respuesta.put(
                "status",
                ex.getStatusCode().value()
        );
        respuesta.put(
                "error",
                "Error en servicio backend"
        );
        respuesta.put(
                "mensaje",
                ex.getResponseBodyAsString()
        );

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(respuesta);
    }

    @ExceptionHandler(ServicioNoDisponibleException.class)
    public ResponseEntity<Map<String, Object>>
    manejarServicioNoDisponible(
            ServicioNoDisponibleException ex
    ) {
        Map<String, Object> respuesta = new LinkedHashMap<>();

        respuesta.put(
                "timestamp",
                LocalDateTime.now()
        );
        respuesta.put(
                "status",
                HttpStatus.SERVICE_UNAVAILABLE.value()
        );
        respuesta.put(
                "error",
                "Servicio no disponible"
        );
        respuesta.put(
                "mensaje",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(respuesta);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>>
    manejarArgumentoInvalido(
            IllegalArgumentException ex
    ) {
        Map<String, Object> respuesta = new LinkedHashMap<>();

        respuesta.put(
                "timestamp",
                LocalDateTime.now()
        );
        respuesta.put(
                "status",
                HttpStatus.BAD_REQUEST.value()
        );
        respuesta.put(
                "error",
                "Solicitud inválida"
        );
        respuesta.put(
                "mensaje",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(respuesta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>>
    manejarErrorGeneral(
            Exception ex
    ) {
        Map<String, Object> respuesta = new LinkedHashMap<>();

        respuesta.put(
                "timestamp",
                LocalDateTime.now()
        );
        respuesta.put(
                "status",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        respuesta.put(
                "error",
                "Error interno"
        );
        respuesta.put(
                "mensaje",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(respuesta);
    }
}