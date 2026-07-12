package com.duoc.cursos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExamenResponse(
        Long id,
        Long idCurso,
        String titulo,
        String pregunta,
        BigDecimal puntajeMaximo,
        Boolean activo,
        LocalDateTime fechaCreacion
) {
}