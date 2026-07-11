package com.duoc.bff.controller;

import com.duoc.bff.dto.InscripcionMessage;
import com.duoc.bff.service.PlataformaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
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
    public Map<String, Object> buscarCurso(
            @PathVariable Long id
    ) {
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

    @PostMapping(
            value = "/cursos/{idCurso}/archivos",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, Object>> subirArchivo(
            @PathVariable Long idCurso,
            @RequestParam("archivo") MultipartFile archivo
    ) {
        Map<String, Object> respuesta =
                plataformaService.subirArchivo(idCurso, archivo);

        return ResponseEntity.ok(respuesta);
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
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(contenido);
    }
}