package com.duoc.cursos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CalificacionResponse(
        Long id,
        Long idExamen,
        Long idCurso,
        Long idEstudiante,
        String respuestaEstudiante,
        BigDecimal puntaje,
        LocalDateTime fechaRespuesta
) {
}