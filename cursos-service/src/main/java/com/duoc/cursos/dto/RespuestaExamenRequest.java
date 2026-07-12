package com.duoc.cursos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record RespuestaExamenRequest(

        @NotNull(message = "El estudiante es obligatorio")
        @Positive(message = "El identificador del estudiante debe ser positivo")
        Long idEstudiante,

        @NotBlank(message = "La respuesta es obligatoria")
        @Size(
                max = 1000,
                message = "La respuesta no puede superar 1000 caracteres"
        )
        String respuesta

) {
}