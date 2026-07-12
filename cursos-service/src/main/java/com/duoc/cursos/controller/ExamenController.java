package com.duoc.cursos.controller;

import com.duoc.cursos.dto.CalificacionResponse;
import com.duoc.cursos.dto.ExamenRequest;
import com.duoc.cursos.dto.ExamenResponse;
import com.duoc.cursos.dto.RespuestaExamenRequest;
import com.duoc.cursos.service.CalificacionService;
import com.duoc.cursos.service.ExamenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExamenController {

    private final ExamenService examenService;
    private final CalificacionService calificacionService;

    @GetMapping("/cursos/{idCurso}/examenes")
    public List<ExamenResponse> listarPorCurso(
            @PathVariable Long idCurso
    ) {
        return examenService.listarPorCurso(idCurso);
    }

    @GetMapping("/examenes/{idExamen}")
    public ExamenResponse buscarPorId(
            @PathVariable Long idExamen
    ) {
        return examenService.buscarPorId(idExamen);
    }

    @PostMapping("/cursos/{idCurso}/examenes")
    @ResponseStatus(HttpStatus.CREATED)
    public ExamenResponse crear(
            @PathVariable Long idCurso,
            @Valid @RequestBody ExamenRequest request
    ) {
        return examenService.crear(
                idCurso,
                request
        );
    }

    @PutMapping("/examenes/{idExamen}")
    public ExamenResponse actualizar(
            @PathVariable Long idExamen,
            @Valid @RequestBody ExamenRequest request
    ) {
        return examenService.actualizar(
                idExamen,
                request
        );
    }

    @DeleteMapping("/examenes/{idExamen}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desactivar(
            @PathVariable Long idExamen
    ) {
        examenService.desactivar(idExamen);
    }

    @PostMapping("/examenes/{idExamen}/respuestas")
    @ResponseStatus(HttpStatus.CREATED)
    public CalificacionResponse responder(
            @PathVariable Long idExamen,
            @Valid @RequestBody RespuestaExamenRequest request
    ) {
        return calificacionService.responderExamen(
                idExamen,
                request
        );
    }
}