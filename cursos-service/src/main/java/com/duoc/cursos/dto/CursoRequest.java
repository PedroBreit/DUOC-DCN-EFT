package com.duoc.cursos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CursoRequest(

        @NotBlank
        @Size(max = 120)
        String nombre,

        @NotBlank
        @Size(max = 500)
        String descripcion,

        @NotBlank
        @Size(max = 120)
        String instructor,

        @NotNull
        @Min(1)
        Integer cupoMaximo
) {
}