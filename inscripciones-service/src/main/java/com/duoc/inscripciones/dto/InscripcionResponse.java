package com.duoc.inscripciones.dto;

import java.time.LocalDateTime;

public record InscripcionResponse(
        Long id,
        Long idEstudiante,
        String nombreEstudiante,
        String correoEstudiante,
        Long idCurso,
        String estado,
        LocalDateTime fechaInscripcion
) {
}