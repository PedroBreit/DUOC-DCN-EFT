package com.duoc.inscripciones.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(
            RecursoNoEncontradoException ex
    ) {
        return crearRespuesta(
                HttpStatus.NOT_FOUND,
                "Recurso no encontrado",
                ex.getMessage()
        );
    }

    @ExceptionHandler(InscripcionDuplicadaException.class)
    public ResponseEntity<Map<String, Object>> manejarDuplicada(
            InscripcionDuplicadaException ex
    ) {
        return crearRespuesta(
                HttpStatus.CONFLICT,
                "Inscripción duplicada",
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidaciones(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> detalles = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        detalles.put(error.getField(), error.getDefaultMessage())
                );

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
        respuesta.put("error", "Datos inválidos");
        respuesta.put("detalles", detalles);

        return ResponseEntity.badRequest().body(respuesta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarGeneral(Exception ex) {
        return crearRespuesta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno",
                ex.getMessage()
        );
    }

    private ResponseEntity<Map<String, Object>> crearRespuesta(
            HttpStatus estado,
            String error,
            String mensaje
    ) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", estado.value());
        respuesta.put("error", error);
        respuesta.put("mensaje", mensaje);

        return ResponseEntity.status(estado).body(respuesta);
    }
}