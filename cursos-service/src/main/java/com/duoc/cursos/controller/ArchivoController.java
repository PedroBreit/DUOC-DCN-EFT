package com.duoc.cursos.controller;

import com.duoc.cursos.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/archivos")
@RequiredArgsConstructor
public class ArchivoController {

    private final S3Service s3Service;

    @PostMapping("/cursos/{idCurso}")
    public ResponseEntity<Map<String, String>> subirArchivo(
            @PathVariable Long idCurso,
            @RequestParam("archivo") MultipartFile archivo
    ) {
        String key = s3Service.subirArchivo(idCurso, archivo);

        return ResponseEntity.ok(
                Map.of(
                        "mensaje", "Archivo almacenado correctamente",
                        "key", key
                )
        );
    }

    @GetMapping
    public ResponseEntity<byte[]> descargarArchivo(
            @RequestParam String key
    ) {
        byte[] contenido = s3Service.descargarArchivo(key);

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