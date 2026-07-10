package com.duoc.inscripciones.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InscripcionRequest(

        @NotNull
        @Positive
        Long idEstudiante,

        @NotBlank
        String nombreEstudiante,

        @NotBlank
        @Email
        String correoEstudiante,

        @NotNull
        @Positive
        Long idCurso
) {
}