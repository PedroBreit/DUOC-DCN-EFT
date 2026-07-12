package com.duoc.cursos.controller;

import com.duoc.cursos.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/archivos")
@RequiredArgsConstructor
public class ArchivoController {

    private final S3Service s3Service;

    @PostMapping(
            value = "/cursos/{idCurso}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, String>> subirArchivo(
            @PathVariable Long idCurso,
            @RequestParam("archivo") MultipartFile archivo
    ) {
        String key =
                s3Service.subirArchivo(idCurso, archivo);

        return ResponseEntity.ok(
                Map.of(
                        "mensaje",
                        "Archivo almacenado correctamente",
                        "key",
                        key
                )
        );
    }

    @GetMapping("/cursos/{idCurso}")
    public List<Map<String, Object>> listarArchivos(
            @PathVariable Long idCurso
    ) {
        return s3Service.listarArchivos(idCurso);
    }

    @PutMapping(
            value = "/cursos/{idCurso}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, String>>
    reemplazarArchivo(
            @PathVariable Long idCurso,
            @RequestParam String key,
            @RequestParam("archivo") MultipartFile archivo
    ) {
        String keyActualizado =
                s3Service.reemplazarArchivo(
                        idCurso,
                        key,
                        archivo
                );

        return ResponseEntity.ok(
                Map.of(
                        "mensaje",
                        "Archivo reemplazado correctamente",
                        "key",
                        keyActualizado
                )
        );
    }

    @DeleteMapping("/cursos/{idCurso}")
    public ResponseEntity<Void> eliminarArchivo(
            @PathVariable Long idCurso,
            @RequestParam String key
    ) {
        s3Service.eliminarArchivo(idCurso, key);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<byte[]> descargarArchivo(
            @RequestParam String key
    ) {
        byte[] contenido =
                s3Service.descargarArchivo(key);

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
}