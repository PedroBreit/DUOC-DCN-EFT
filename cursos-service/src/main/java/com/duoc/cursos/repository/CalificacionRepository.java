package com.duoc.cursos.repository;

import com.duoc.cursos.model.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalificacionRepository
        extends JpaRepository<Calificacion, Long> {

    boolean existsByExamenIdAndIdEstudiante(
            Long idExamen,
            Long idEstudiante
    );

    List<Calificacion>
    findByIdEstudianteOrderByFechaRespuestaDesc(
            Long idEstudiante
    );

    List<Calificacion>
    findByExamenCursoIdOrderByFechaRespuestaDesc(
            Long idCurso
    );
}