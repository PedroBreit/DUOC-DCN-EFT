package com.duoc.bff.controller;

import com.duoc.bff.dto.InscripcionMessage;
import com.duoc.bff.service.PlataformaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/plataforma")
@RequiredArgsConstructor
public class PlataformaController {

    private final PlataformaService plataformaService;

    @GetMapping("/cursos")
    public List<Map<String, Object>> listarCursos() {
        return plataformaService.listarCursos();
    }

    @GetMapping("/cursos/{id}")
    public Map<String, Object> buscarCurso(@PathVariable Long id) {
        return plataformaService.buscarCurso(id);
    }

    @GetMapping("/inscripciones")
    public List<Map<String, Object>> listarInscripciones() {
        return plataformaService.listarInscripciones();
    }

    @PostMapping("/inscripciones")
    public Map<String, Object> publicarInscripcion(
            @RequestBody InscripcionMessage mensaje
    ) {
        return plataformaService.publicarInscripcion(mensaje);
    }
}