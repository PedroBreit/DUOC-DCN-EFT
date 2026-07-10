package com.duoc.cursos.dto;

import java.time.LocalDateTime;

public record CursoResponse(
        Long id,
        String nombre,
        String descripcion,
        String instructor,
        Integer cupoMaximo,
        Boolean activo,
        LocalDateTime fechaCreacion
) {
}