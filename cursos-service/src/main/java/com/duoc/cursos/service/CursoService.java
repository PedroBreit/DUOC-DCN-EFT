package com.duoc.cursos.service;

import com.duoc.cursos.dto.CursoRequest;
import com.duoc.cursos.dto.CursoResponse;
import com.duoc.cursos.model.Curso;
import com.duoc.cursos.repository.CursoRepository;
import com.duoc.cursos.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;

    public List<CursoResponse> listarActivos() {
        return cursoRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CursoResponse buscarPorId(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Curso no encontrado con id: " + id)
                );

        return toResponse(curso);
    }

    public CursoResponse crear(CursoRequest request) {
        Curso curso = Curso.builder()
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .instructor(request.instructor())
                .cupoMaximo(request.cupoMaximo())
                .activo(true)
                .build();

        return toResponse(cursoRepository.save(curso));
    }

    public CursoResponse actualizar(Long id, CursoRequest request) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Curso no encontrado con id: " + id)
                );

        curso.setNombre(request.nombre());
        curso.setDescripcion(request.descripcion());
        curso.setInstructor(request.instructor());
        curso.setCupoMaximo(request.cupoMaximo());

        return toResponse(cursoRepository.save(curso));
    }

    public void desactivar(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Curso no encontrado con id: " + id)
                );

        curso.setActivo(false);
        cursoRepository.save(curso);
    }

    private CursoResponse toResponse(Curso curso) {
        return new CursoResponse(
                curso.getId(),
                curso.getNombre(),
                curso.getDescripcion(),
                curso.getInstructor(),
                curso.getCupoMaximo(),
                curso.getActivo(),
                curso.getFechaCreacion()
        );
    }
}