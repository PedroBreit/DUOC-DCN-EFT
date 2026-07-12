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

    // =====================================================
    // CURSOS
    // =====================================================

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
                    .uri(
                            cursosUrl + "/api/cursos/{id}",
                            id
                    )
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con cursos-service"
            );
        }
    }

    public Map<String, Object> crearCurso(
            Map<String, Object> curso
    ) {
        try {
            return restClient.post()
                    .uri(cursosUrl + "/api/cursos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(curso)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con cursos-service"
            );
        }
    }

    public Map<String, Object> actualizarCurso(
            Long id,
            Map<String, Object> curso
    ) {
        try {
            return restClient.put()
                    .uri(
                            cursosUrl + "/api/cursos/{id}",
                            id
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(curso)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con cursos-service"
            );
        }
    }

    public void eliminarCurso(Long id) {
        try {
            restClient.delete()
                    .uri(
                            cursosUrl + "/api/cursos/{id}",
                            id
                    )
                    .retrieve()
                    .toBodilessEntity();

        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con cursos-service"
            );
        }
    }

    // =====================================================
    // INSCRIPCIONES
    // =====================================================

    public List<Map<String, Object>> listarInscripciones() {
        try {
            return restClient.get()
                    .uri(
                            inscripcionesUrl
                                    + "/api/inscripciones"
                    )
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
                    .uri(
                            inscripcionesUrl
                                    + "/api/inscripciones/mensajes"
                    )
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

    public Map<String, Object> consumirMensajeError() {
        try {
                return restClient.post()
                        .uri(
                                inscripcionesUrl
                                        + "/api/inscripciones/mensajes/"
                                        + "errores/consumir"
                        )
                        .retrieve()
                        .body(new ParameterizedTypeReference<>() {
                        });

        } catch (ResourceAccessException ex) {
                throw new ServicioNoDisponibleException(
                        "No fue posible conectar con inscripciones-service"
                );
        }
    }

    // =====================================================
    // ARCHIVOS
    // =====================================================

    public Map<String, Object> subirArchivo(
            Long idCurso,
            MultipartFile archivo
    ) {
        try {
            MultiValueMap<String, Object> multipart =
                    construirMultipart(archivo);

            return restClient.post()
                    .uri(
                            cursosUrl
                                    + "/api/archivos/cursos/{idCurso}",
                            idCurso
                    )
                    .contentType(
                            MediaType.MULTIPART_FORM_DATA
                    )
                    .body(multipart)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con cursos-service"
            );
        }
    }

    public List<Map<String, Object>> listarArchivos(
            Long idCurso
    ) {
        try {
            return restClient.get()
                    .uri(
                            cursosUrl
                                    + "/api/archivos/cursos/{idCurso}",
                            idCurso
                    )
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con cursos-service"
            );
        }
    }

    public Map<String, Object> reemplazarArchivo(
            Long idCurso,
            String key,
            MultipartFile archivo
    ) {
        try {
            MultiValueMap<String, Object> multipart =
                    construirMultipart(archivo);

            return restClient.put()
                    .uri(
                            cursosUrl
                                    + "/api/archivos/cursos/{idCurso}"
                                    + "?key={key}",
                            idCurso,
                            key
                    )
                    .contentType(
                            MediaType.MULTIPART_FORM_DATA
                    )
                    .body(multipart)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con cursos-service"
            );
        }
    }

    public void eliminarArchivo(
            Long idCurso,
            String key
    ) {
        try {
            restClient.delete()
                    .uri(
                            cursosUrl
                                    + "/api/archivos/cursos/{idCurso}"
                                    + "?key={key}",
                            idCurso,
                            key
                    )
                    .retrieve()
                    .toBodilessEntity();

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
                            cursosUrl
                                    + "/api/archivos?key={key}",
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

    // =====================================================
    // EXÁMENES
    // =====================================================

    public List<Map<String, Object>> listarExamenes(
            Long idCurso
    ) {
        try {
            return restClient.get()
                    .uri(
                            cursosUrl
                                    + "/api/cursos/{idCurso}/examenes",
                            idCurso
                    )
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con cursos-service"
            );
        }
    }

    public Map<String, Object> buscarExamen(
            Long idExamen
    ) {
        try {
            return restClient.get()
                    .uri(
                            cursosUrl
                                    + "/api/examenes/{idExamen}",
                            idExamen
                    )
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con cursos-service"
            );
        }
    }

    public Map<String, Object> crearExamen(
            Long idCurso,
            Map<String, Object> examen
    ) {
        try {
            return restClient.post()
                    .uri(
                            cursosUrl
                                    + "/api/cursos/{idCurso}/examenes",
                            idCurso
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(examen)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con cursos-service"
            );
        }
    }

    public Map<String, Object> actualizarExamen(
            Long idExamen,
            Map<String, Object> examen
    ) {
        try {
            return restClient.put()
                    .uri(
                            cursosUrl
                                    + "/api/examenes/{idExamen}",
                            idExamen
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(examen)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con cursos-service"
            );
        }
    }

    public void eliminarExamen(Long idExamen) {
        try {
            restClient.delete()
                    .uri(
                            cursosUrl
                                    + "/api/examenes/{idExamen}",
                            idExamen
                    )
                    .retrieve()
                    .toBodilessEntity();

        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con cursos-service"
            );
        }
    }

    public Map<String, Object> responderExamen(
            Long idExamen,
            Map<String, Object> respuesta
    ) {
        try {
            return restClient.post()
                    .uri(
                            cursosUrl
                                    + "/api/examenes/{idExamen}/respuestas",
                            idExamen
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(respuesta)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con cursos-service"
            );
        }
    }

    // =====================================================
    // CALIFICACIONES
    // =====================================================

    public List<Map<String, Object>>
    listarCalificacionesEstudiante(
            Long idEstudiante
    ) {
        try {
            return restClient.get()
                    .uri(
                            cursosUrl
                                    + "/api/calificaciones/estudiantes/"
                                    + "{idEstudiante}",
                            idEstudiante
                    )
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con cursos-service"
            );
        }
    }

    public List<Map<String, Object>>
    listarCalificacionesCurso(
            Long idCurso
    ) {
        try {
            return restClient.get()
                    .uri(
                            cursosUrl
                                    + "/api/cursos/{idCurso}/calificaciones",
                            idCurso
                    )
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con cursos-service"
            );
        }
    }

    public Map<String, Object> actualizarCalificacion(
            Long idCalificacion,
            Map<String, Object> calificacion
    ) {
        try {
            return restClient.put()
                    .uri(
                            cursosUrl
                                    + "/api/calificaciones/{idCalificacion}",
                            idCalificacion
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(calificacion)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException(
                    "No fue posible conectar con cursos-service"
            );
        }
    }

    // =====================================================
    // UTILIDADES
    // =====================================================

    private MultiValueMap<String, Object> construirMultipart(
            MultipartFile archivo
    ) {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException(
                    "El archivo no puede estar vacío"
            );
        }

        try {
            ByteArrayResource recurso =
                    new ByteArrayResource(
                            archivo.getBytes()
                    ) {
                        @Override
                        public String getFilename() {
                            return archivo.getOriginalFilename();
                        }
                    };

            MultiValueMap<String, Object> multipart =
                    new LinkedMultiValueMap<>();

            multipart.add("archivo", recurso);

            return multipart;

        } catch (IOException ex) {
            throw new IllegalStateException(
                    "No fue posible leer el archivo enviado",
                    ex
            );
        }
    }
}