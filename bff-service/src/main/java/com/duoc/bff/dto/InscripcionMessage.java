package com.duoc.bff.dto;

public record InscripcionMessage(
        Long idEstudiante,
        String nombreEstudiante,
        String correoEstudiante,
        Long idCurso,
        Boolean forzarError
) {
}