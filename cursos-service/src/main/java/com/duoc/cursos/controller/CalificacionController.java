package com.duoc.cursos.controller;

import com.duoc.cursos.dto.CalificacionResponse;
import com.duoc.cursos.dto.CalificacionUpdateRequest;
import com.duoc.cursos.service.CalificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CalificacionController {

    private final CalificacionService calificacionService;

    @GetMapping(
            "/calificaciones/estudiantes/{idEstudiante}"
    )
    public List<CalificacionResponse> listarPorEstudiante(
            @PathVariable Long idEstudiante
    ) {
        return calificacionService.listarPorEstudiante(
                idEstudiante
        );
    }

    @GetMapping(
            "/cursos/{idCurso}/calificaciones"
    )
    public List<CalificacionResponse> listarPorCurso(
            @PathVariable Long idCurso
    ) {
        return calificacionService.listarPorCurso(
                idCurso
        );
    }

    @PutMapping(
            "/calificaciones/{idCalificacion}"
    )
    public CalificacionResponse actualizarPuntaje(
            @PathVariable Long idCalificacion,
            @Valid @RequestBody
            CalificacionUpdateRequest request
    ) {
        return calificacionService.actualizarPuntaje(
                idCalificacion,
                request
        );
    }
}