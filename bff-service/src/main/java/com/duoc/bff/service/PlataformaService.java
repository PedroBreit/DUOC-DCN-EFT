package com.duoc.bff.service;

import com.duoc.bff.dto.InscripcionMessage;
import com.duoc.bff.exception.ServicioNoDisponibleException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

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
}