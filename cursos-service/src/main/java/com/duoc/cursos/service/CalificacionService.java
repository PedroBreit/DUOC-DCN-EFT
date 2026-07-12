package com.duoc.cursos.service;

import com.duoc.cursos.dto.CalificacionResponse;
import com.duoc.cursos.dto.CalificacionUpdateRequest;
import com.duoc.cursos.dto.RespuestaExamenRequest;
import com.duoc.cursos.exception.RecursoNoEncontradoException;
import com.duoc.cursos.model.Calificacion;
import com.duoc.cursos.model.Examen;
import com.duoc.cursos.repository.CalificacionRepository;
import com.duoc.cursos.repository.ExamenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final ExamenRepository examenRepository;

    @Transactional
    public CalificacionResponse responderExamen(
            Long idExamen,
            RespuestaExamenRequest request
    ) {
        Examen examen =
                buscarExamenActivo(idExamen);

        boolean yaRespondido =
                calificacionRepository
                        .existsByExamenIdAndIdEstudiante(
                                idExamen,
                                request.idEstudiante()
                        );

        if (yaRespondido) {
            throw new IllegalArgumentException(
                    "El estudiante ya respondió este examen"
            );
        }

        String respuestaEstudiante =
                request.respuesta().trim();

        boolean respuestaCorrecta =
                examen.getRespuestaCorrecta()
                        .trim()
                        .equalsIgnoreCase(
                                respuestaEstudiante
                        );

        BigDecimal puntaje = respuestaCorrecta
                ? examen.getPuntajeMaximo()
                : BigDecimal.ZERO;

        Calificacion calificacion =
                Calificacion.builder()
                        .examen(examen)
                        .idEstudiante(
                                request.idEstudiante()
                        )
                        .respuestaEstudiante(
                                respuestaEstudiante
                        )
                        .puntaje(puntaje)
                        .build();

        return toResponse(
                calificacionRepository.save(
                        calificacion
                )
        );
    }

    @Transactional(readOnly = true)
    public List<CalificacionResponse>
    listarPorEstudiante(
            Long idEstudiante
    ) {
        validarIdEstudiante(idEstudiante);

        return calificacionRepository
                .findByIdEstudianteOrderByFechaRespuestaDesc(
                        idEstudiante
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CalificacionResponse> listarPorCurso(
            Long idCurso
    ) {
        if (idCurso == null || idCurso <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del curso no es válido"
            );
        }

        return calificacionRepository
                .findByExamenCursoIdOrderByFechaRespuestaDesc(
                        idCurso
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CalificacionResponse actualizarPuntaje(
            Long idCalificacion,
            CalificacionUpdateRequest request
    ) {
        Calificacion calificacion =
                calificacionRepository
                        .findById(idCalificacion)
                        .orElseThrow(() ->
                                new RecursoNoEncontradoException(
                                        "Calificación no encontrada con id: "
                                                + idCalificacion
                                )
                        );

        BigDecimal puntajeMaximo =
                calificacion
                        .getExamen()
                        .getPuntajeMaximo();

        if (request.puntaje()
                .compareTo(puntajeMaximo) > 0) {

            throw new IllegalArgumentException(
                    "El puntaje no puede superar el máximo del examen: "
                            + puntajeMaximo
            );
        }

        calificacion.setPuntaje(
                request.puntaje()
        );

        return toResponse(
                calificacionRepository.save(
                        calificacion
                )
        );
    }

    private Examen buscarExamenActivo(
            Long idExamen
    ) {
        if (idExamen == null || idExamen <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del examen no es válido"
            );
        }

        return examenRepository
                .findByIdAndActivoTrue(idExamen)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Examen no encontrado con id: "
                                        + idExamen
                        )
                );
    }

    private void validarIdEstudiante(
            Long idEstudiante
    ) {
        if (idEstudiante == null
                || idEstudiante <= 0) {

            throw new IllegalArgumentException(
                    "El identificador del estudiante no es válido"
            );
        }
    }

    private CalificacionResponse toResponse(
            Calificacion calificacion
    ) {
        Examen examen =
                calificacion.getExamen();

        return new CalificacionResponse(
                calificacion.getId(),
                examen.getId(),
                examen.getCurso().getId(),
                calificacion.getIdEstudiante(),
                calificacion.getRespuestaEstudiante(),
                calificacion.getPuntaje(),
                calificacion.getFechaRespuesta()
        );
    }
}