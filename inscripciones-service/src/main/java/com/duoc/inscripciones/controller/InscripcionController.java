package com.duoc.inscripciones.controller;

import com.duoc.inscripciones.dto.InscripcionMessage;
import com.duoc.inscripciones.dto.InscripcionRequest;
import com.duoc.inscripciones.dto.InscripcionResponse;
import com.duoc.inscripciones.messaging.InscripcionProducer;
import com.duoc.inscripciones.service.InscripcionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inscripciones")
@RequiredArgsConstructor
public class InscripcionController {

    private final InscripcionService inscripcionService;
    private final InscripcionProducer inscripcionProducer;

    @GetMapping
    public List<InscripcionResponse> listar() {
        return inscripcionService.listar();
    }

    @GetMapping("/{id}")
    public InscripcionResponse buscarPorId(@PathVariable Long id) {
        return inscripcionService.buscarPorId(id);
    }

    @GetMapping("/estudiante/{idEstudiante}")
    public List<InscripcionResponse> listarPorEstudiante(
            @PathVariable Long idEstudiante
    ) {
        return inscripcionService.listarPorEstudiante(idEstudiante);
    }

    @GetMapping("/curso/{idCurso}")
    public List<InscripcionResponse> listarPorCurso(
            @PathVariable Long idCurso
    ) {
        return inscripcionService.listarPorCurso(idCurso);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InscripcionResponse crear(
            @Valid @RequestBody InscripcionRequest request
    ) {
        return inscripcionService.crear(request);
    }

    @PostMapping("/mensajes")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, Object> publicarMensaje(
            @RequestBody InscripcionMessage mensaje
    ) {
        inscripcionProducer.publicar(mensaje);

        return Map.of(
                "mensaje", "Solicitud enviada a RabbitMQ",
                "estado", "EN_PROCESO"
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        inscripcionService.eliminar(id);
    }
}