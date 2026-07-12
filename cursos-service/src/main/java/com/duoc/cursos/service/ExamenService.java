package com.duoc.cursos.service;

import com.duoc.cursos.dto.ExamenRequest;
import com.duoc.cursos.dto.ExamenResponse;
import com.duoc.cursos.exception.RecursoNoEncontradoException;
import com.duoc.cursos.model.Curso;
import com.duoc.cursos.model.Examen;
import com.duoc.cursos.repository.CursoRepository;
import com.duoc.cursos.repository.ExamenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamenService {

    private final ExamenRepository examenRepository;
    private final CursoRepository cursoRepository;

    @Transactional(readOnly = true)
    public List<ExamenResponse> listarPorCurso(
            Long idCurso
    ) {
        validarCurso(idCurso);

        return examenRepository
                .findByCursoIdAndActivoTrueOrderByIdAsc(idCurso)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ExamenResponse buscarPorId(Long idExamen) {
        return toResponse(
                buscarExamenActivo(idExamen)
        );
    }

    @Transactional
    public ExamenResponse crear(
            Long idCurso,
            ExamenRequest request
    ) {
        Curso curso = validarCurso(idCurso);

        if (!Boolean.TRUE.equals(curso.getActivo())) {
            throw new IllegalArgumentException(
                    "No se puede crear un examen en un curso inactivo"
            );
        }

        Examen examen = Examen.builder()
                .curso(curso)
                .titulo(request.titulo().trim())
                .pregunta(request.pregunta().trim())
                .respuestaCorrecta(
                        request.respuestaCorrecta().trim()
                )
                .puntajeMaximo(request.puntajeMaximo())
                .activo(true)
                .build();

        return toResponse(
                examenRepository.save(examen)
        );
    }

    @Transactional
    public ExamenResponse actualizar(
            Long idExamen,
            ExamenRequest request
    ) {
        Examen examen =
                buscarExamenActivo(idExamen);

        examen.setTitulo(request.titulo().trim());
        examen.setPregunta(request.pregunta().trim());
        examen.setRespuestaCorrecta(
                request.respuestaCorrecta().trim()
        );
        examen.setPuntajeMaximo(
                request.puntajeMaximo()
        );

        return toResponse(
                examenRepository.save(examen)
        );
    }

    @Transactional
    public void desactivar(Long idExamen) {
        Examen examen =
                buscarExamenActivo(idExamen);

        examen.setActivo(false);
        examenRepository.save(examen);
    }

    private Curso validarCurso(Long idCurso) {
        if (idCurso == null || idCurso <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del curso no es válido"
            );
        }

        return cursoRepository.findById(idCurso)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Curso no encontrado con id: "
                                        + idCurso
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

    private ExamenResponse toResponse(
            Examen examen
    ) {
        return new ExamenResponse(
                examen.getId(),
                examen.getCurso().getId(),
                examen.getTitulo(),
                examen.getPregunta(),
                examen.getPuntajeMaximo(),
                examen.getActivo(),
                examen.getFechaCreacion()
        );
    }
}