package com.duoc.cursos.repository;

import com.duoc.cursos.model.Examen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExamenRepository
        extends JpaRepository<Examen, Long> {

    List<Examen> findByCursoIdAndActivoTrueOrderByIdAsc(
            Long idCurso
    );

    Optional<Examen> findByIdAndActivoTrue(
            Long id
    );
}