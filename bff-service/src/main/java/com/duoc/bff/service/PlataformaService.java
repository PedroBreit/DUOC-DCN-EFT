package com.duoc.bff.service;

import com.duoc.bff.dto.InscripcionMessage;
import com.duoc.bff.exception.ServicioNoDisponibleException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlataformaService {

    private final RestClient restClient;

    @Value("${services.cursos.url}")
    private String cursosUrl;

    @Value("${services.inscripciones.url}")
    private String inscripcionesUrl;

    public List<Map<String, Object>> listarCursos() {
        try {
            return restClient.get()
                    .uri(cursosUrl + "/api/cursos")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con cursos-service"
            );
        }
    }

    public Map<String, Object> buscarCurso(Long id) {
        try {
            return restClient.get()
                    .uri(cursosUrl + "/api/cursos/" + id)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con cursos-service"
            );
        }
    }

    public List<Map<String, Object>> listarInscripciones() {
        try {
            return restClient.get()
                    .uri(inscripcionesUrl + "/api/inscripciones")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con inscripciones-service"
            );
        }
    }

    public Map<String, Object> publicarInscripcion(
            InscripcionMessage mensaje
    ) {
        try {
            return restClient.post()
                    .uri(inscripcionesUrl + "/api/inscripciones/mensajes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mensaje)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con inscripciones-service"
            );
        }
    }

    public Map<String, Object> subirArchivo(
            Long idCurso,
            MultipartFile archivo
    ) {
        try {
            ByteArrayResource recurso = new ByteArrayResource(
                    archivo.getBytes()
            ) {
                @Override
                public String getFilename() {
                    return archivo.getOriginalFilename();
                }
            };

            MultiValueMap<String, Object> body =
                    new LinkedMultiValueMap<>();

            body.add("archivo", recurso);

            return restClient.post()
                    .uri(
                            cursosUrl
                                    + "/api/archivos/cursos/"
                                    + idCurso
                    )
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } catch (IOException ex) {
            throw new IllegalStateException(
                    "No fue posible leer el archivo enviado",
                    ex
            );
        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con cursos-service"
            );
        }
    }

    public byte[] descargarArchivo(String key) {
        try {
            return restClient.get()
                .uri(
                    cursosUrl + "/api/archivos?key={key}",
                    key
                )
                .retrieve()
                .body(byte[].class);
        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                "No fue posible conectar con cursos-service"
            );
        }
    }
}