package com.duoc.cursos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ExamenRequest(

        @NotBlank(message = "El título es obligatorio")
        @Size(
                max = 150,
                message = "El título no puede superar 150 caracteres"
        )
        String titulo,

        @NotBlank(message = "La pregunta es obligatoria")
        @Size(
                max = 1000,
                message = "La pregunta no puede superar 1000 caracteres"
        )
        String pregunta,

        @NotBlank(message = "La respuesta correcta es obligatoria")
        @Size(
                max = 1000,
                message = "La respuesta correcta no puede superar 1000 caracteres"
        )
        String respuestaCorrecta,

        @NotNull(message = "El puntaje máximo es obligatorio")
        @DecimalMin(
                value = "0.01",
                message = "El puntaje máximo debe ser mayor que cero"
        )
        BigDecimal puntajeMaximo

) {
}