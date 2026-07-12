package com.duoc.cursos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CalificacionUpdateRequest(

        @NotNull(message = "El puntaje es obligatorio")
        @DecimalMin(
                value = "0.00",
                message = "El puntaje no puede ser negativo"
        )
        BigDecimal puntaje

) {
}