package com.duoc.cursos.controller;

import com.duoc.cursos.dto.CursoRequest;
import com.duoc.cursos.dto.CursoResponse;
import com.duoc.cursos.service.CursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @GetMapping
    public List<CursoResponse> listar() {
        return cursoService.listarActivos();
    }

    @GetMapping("/{id}")
    public CursoResponse buscarPorId(@PathVariable Long id) {
        return cursoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CursoResponse crear(@Valid @RequestBody CursoRequest request) {
        return cursoService.crear(request);
    }

    @PutMapping("/{id}")
    public CursoResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CursoRequest request
    ) {
        return cursoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desactivar(@PathVariable Long id) {
        cursoService.desactivar(id);
    }
}