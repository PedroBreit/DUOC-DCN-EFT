package com.duoc.inscripciones.controller;

import com.duoc.inscripciones.dto.InscripcionMessage;
import com.duoc.inscripciones.dto.InscripcionRequest;
import com.duoc.inscripciones.dto.InscripcionResponse;
import com.duoc.inscripciones.messaging.InscripcionErrorConsumer;
import com.duoc.inscripciones.messaging.InscripcionProducer;
import com.duoc.inscripciones.service.InscripcionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inscripciones")
@RequiredArgsConstructor
public class InscripcionController {

    private final InscripcionService inscripcionService;
    private final InscripcionProducer inscripcionProducer;
    private final InscripcionErrorConsumer inscripcionErrorConsumer;

    @GetMapping
    public List<InscripcionResponse> listar() {
        return inscripcionService.listar();
    }

    @GetMapping("/{id}")
    public InscripcionResponse buscarPorId(
            @PathVariable Long id
    ) {
        return inscripcionService.buscarPorId(id);
    }

    @GetMapping("/estudiante/{idEstudiante}")
    public List<InscripcionResponse> listarPorEstudiante(
            @PathVariable Long idEstudiante
    ) {
        return inscripcionService.listarPorEstudiante(
                idEstudiante
        );
    }

    @GetMapping("/curso/{idCurso}")
    public List<InscripcionResponse> listarPorCurso(
            @PathVariable Long idCurso
    ) {
        return inscripcionService.listarPorCurso(
                idCurso
        );
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
                "mensaje",
                "Solicitud enviada a RabbitMQ",
                "estado",
                "EN_PROCESO"
        );
    }

    @PostMapping("/mensajes/errores/consumir")
    public Map<String, Object> consumirMensajeError() {
        return inscripcionErrorConsumer.consumirUno();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(
            @PathVariable Long id
    ) {
        inscripcionService.eliminar(id);
    }
}