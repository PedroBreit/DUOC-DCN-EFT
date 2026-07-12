package com.duoc.bff.controller;

import com.duoc.bff.dto.InscripcionMessage;
import com.duoc.bff.service.PlataformaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/plataforma")
@RequiredArgsConstructor
public class PlataformaController {

    private final PlataformaService plataformaService;

    // =====================================================
    // CURSOS
    // =====================================================

    @GetMapping("/cursos")
    public List<Map<String, Object>> listarCursos() {
        return plataformaService.listarCursos();
    }

    @GetMapping("/cursos/{id}")
    public Map<String, Object> buscarCurso(
            @PathVariable Long id
    ) {
        return plataformaService.buscarCurso(id);
    }

    @PostMapping("/cursos")
    public ResponseEntity<Map<String, Object>> crearCurso(
            @RequestBody Map<String, Object> curso
    ) {
        Map<String, Object> respuesta =
                plataformaService.crearCurso(curso);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }

    @PutMapping("/cursos/{id}")
    public ResponseEntity<Map<String, Object>> actualizarCurso(
            @PathVariable Long id,
            @RequestBody Map<String, Object> curso
    ) {
        return ResponseEntity.ok(
                plataformaService.actualizarCurso(
                        id,
                        curso
                )
        );
    }

    @DeleteMapping("/cursos/{id}")
    public ResponseEntity<Void> eliminarCurso(
            @PathVariable Long id
    ) {
        plataformaService.eliminarCurso(id);

        return ResponseEntity.noContent().build();
    }

    // =====================================================
    // INSCRIPCIONES
    // =====================================================

    @GetMapping("/inscripciones")
    public List<Map<String, Object>> listarInscripciones() {
        return plataformaService.listarInscripciones();
    }

    @PostMapping("/inscripciones")
    public Map<String, Object> publicarInscripcion(
            @RequestBody InscripcionMessage mensaje
    ) {
        return plataformaService.publicarInscripcion(
                mensaje
        );
    }

    @PostMapping("/inscripciones/mensajes/errores/consumir")
    public Map<String, Object> consumirMensajeError() {
        return plataformaService.consumirMensajeError();
    }

    // =====================================================
    // ARCHIVOS
    // =====================================================

    @PostMapping(
            value = "/cursos/{idCurso}/archivos",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, Object>> subirArchivo(
            @PathVariable Long idCurso,
            @RequestParam("archivo") MultipartFile archivo
    ) {
        return ResponseEntity.ok(
                plataformaService.subirArchivo(
                        idCurso,
                        archivo
                )
        );
    }

    @GetMapping("/cursos/{idCurso}/archivos")
    public ResponseEntity<List<Map<String, Object>>>
    listarArchivos(
            @PathVariable Long idCurso
    ) {
        return ResponseEntity.ok(
                plataformaService.listarArchivos(
                        idCurso
                )
        );
    }

    @PutMapping(
            value = "/cursos/{idCurso}/archivos",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, Object>>
    reemplazarArchivo(
            @PathVariable Long idCurso,
            @RequestParam String key,
            @RequestParam("archivo") MultipartFile archivo
    ) {
        return ResponseEntity.ok(
                plataformaService.reemplazarArchivo(
                        idCurso,
                        key,
                        archivo
                )
        );
    }

    @DeleteMapping("/cursos/{idCurso}/archivos")
    public ResponseEntity<Void> eliminarArchivo(
            @PathVariable Long idCurso,
            @RequestParam String key
    ) {
        plataformaService.eliminarArchivo(
                idCurso,
                key
        );

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/archivos")
    public ResponseEntity<byte[]> descargarArchivo(
            @RequestParam String key
    ) {
        byte[] contenido =
                plataformaService.descargarArchivo(key);

        String nombreArchivo = key.substring(
                key.lastIndexOf('/') + 1
        );

        ContentDisposition disposition =
                ContentDisposition.attachment()
                        .filename(
                                nombreArchivo,
                                StandardCharsets.UTF_8
                        )
                        .build();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        disposition.toString()
                )
                .contentType(
                        MediaType.APPLICATION_OCTET_STREAM
                )
                .body(contenido);
    }

    // =====================================================
    // EXÁMENES
    // =====================================================

    @GetMapping("/cursos/{idCurso}/examenes")
    public ResponseEntity<List<Map<String, Object>>>
    listarExamenes(
            @PathVariable Long idCurso
    ) {
        return ResponseEntity.ok(
                plataformaService.listarExamenes(
                        idCurso
                )
        );
    }

    @GetMapping("/examenes/{idExamen}")
    public ResponseEntity<Map<String, Object>>
    buscarExamen(
            @PathVariable Long idExamen
    ) {
        return ResponseEntity.ok(
                plataformaService.buscarExamen(
                        idExamen
                )
        );
    }

    @PostMapping("/cursos/{idCurso}/examenes")
    public ResponseEntity<Map<String, Object>> crearExamen(
            @PathVariable Long idCurso,
            @RequestBody Map<String, Object> examen
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        plataformaService.crearExamen(
                                idCurso,
                                examen
                        )
                );
    }

    @PutMapping("/examenes/{idExamen}")
    public ResponseEntity<Map<String, Object>>
    actualizarExamen(
            @PathVariable Long idExamen,
            @RequestBody Map<String, Object> examen
    ) {
        return ResponseEntity.ok(
                plataformaService.actualizarExamen(
                        idExamen,
                        examen
                )
        );
    }

    @DeleteMapping("/examenes/{idExamen}")
    public ResponseEntity<Void> eliminarExamen(
            @PathVariable Long idExamen
    ) {
        plataformaService.eliminarExamen(
                idExamen
        );

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/examenes/{idExamen}/respuestas")
    public ResponseEntity<Map<String, Object>>
    responderExamen(
            @PathVariable Long idExamen,
            @RequestBody Map<String, Object> respuesta
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        plataformaService.responderExamen(
                                idExamen,
                                respuesta
                        )
                );
    }

    // =====================================================
    // CALIFICACIONES
    // =====================================================

    @GetMapping(
            "/calificaciones/estudiantes/{idEstudiante}"
    )
    public ResponseEntity<List<Map<String, Object>>>
    listarCalificacionesEstudiante(
            @PathVariable Long idEstudiante
    ) {
        return ResponseEntity.ok(
                plataformaService
                        .listarCalificacionesEstudiante(
                                idEstudiante
                        )
        );
    }

    @GetMapping(
            "/cursos/{idCurso}/calificaciones"
    )
    public ResponseEntity<List<Map<String, Object>>>
    listarCalificacionesCurso(
            @PathVariable Long idCurso
    ) {
        return ResponseEntity.ok(
                plataformaService
                        .listarCalificacionesCurso(
                                idCurso
                        )
        );
    }

    @PutMapping(
            "/calificaciones/{idCalificacion}"
    )
    public ResponseEntity<Map<String, Object>>
    actualizarCalificacion(
            @PathVariable Long idCalificacion,
            @RequestBody Map<String, Object> calificacion
    ) {
        return ResponseEntity.ok(
                plataformaService.actualizarCalificacion(
                        idCalificacion,
                        calificacion
                )
        );
    }
}