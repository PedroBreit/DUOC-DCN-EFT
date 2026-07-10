package com.duoc.inscripciones.repository;

import com.duoc.inscripciones.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    List<Inscripcion> findByIdEstudiante(Long idEstudiante);

    List<Inscripcion> findByIdCurso(Long idCurso);

    boolean existsByIdEstudianteAndIdCurso(Long idEstudiante, Long idCurso);
}