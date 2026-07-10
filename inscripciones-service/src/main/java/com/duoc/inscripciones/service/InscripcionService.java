package com.duoc.inscripciones.service;

import com.duoc.inscripciones.dto.InscripcionRequest;
import com.duoc.inscripciones.dto.InscripcionResponse;
import com.duoc.inscripciones.exception.InscripcionDuplicadaException;
import com.duoc.inscripciones.exception.RecursoNoEncontradoException;
import com.duoc.inscripciones.model.Inscripcion;
import com.duoc.inscripciones.repository.InscripcionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;

    public List<InscripcionResponse> listar() {
        return inscripcionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public InscripcionResponse buscarPorId(Long id) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Inscripción no encontrada con id: " + id
                        )
                );

        return toResponse(inscripcion);
    }

    public List<InscripcionResponse> listarPorEstudiante(Long idEstudiante) {
        return inscripcionRepository.findByIdEstudiante(idEstudiante)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<InscripcionResponse> listarPorCurso(Long idCurso) {
        return inscripcionRepository.findByIdCurso(idCurso)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public InscripcionResponse crear(InscripcionRequest request) {
        boolean existe = inscripcionRepository.existsByIdEstudianteAndIdCurso(
                request.idEstudiante(),
                request.idCurso()
        );

        if (existe) {
            throw new InscripcionDuplicadaException(
                    "El estudiante ya está inscrito en el curso indicado"
            );
        }

        Inscripcion inscripcion = Inscripcion.builder()
                .idEstudiante(request.idEstudiante())
                .nombreEstudiante(request.nombreEstudiante())
                .correoEstudiante(request.correoEstudiante())
                .idCurso(request.idCurso())
                .estado("CONFIRMADA")
                .build();

        return toResponse(inscripcionRepository.save(inscripcion));
    }

    public void eliminar(Long id) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Inscripción no encontrada con id: " + id
                        )
                );

        inscripcionRepository.delete(inscripcion);
    }

    private InscripcionResponse toResponse(Inscripcion inscripcion) {
        return new InscripcionResponse(
                inscripcion.getId(),
                inscripcion.getIdEstudiante(),
                inscripcion.getNombreEstudiante(),
                inscripcion.getCorreoEstudiante(),
                inscripcion.getIdCurso(),
                inscripcion.getEstado(),
                inscripcion.getFechaInscripcion()
        );
    }
}