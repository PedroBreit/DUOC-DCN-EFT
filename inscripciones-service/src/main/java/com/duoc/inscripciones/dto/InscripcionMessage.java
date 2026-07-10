package com.duoc.inscripciones.dto;

public record InscripcionMessage(
        Long idEstudiante,
        String nombreEstudiante,
        String correoEstudiante,
        Long idCurso,
        Boolean forzarError
) {
}