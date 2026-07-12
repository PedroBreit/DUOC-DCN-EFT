package com.duoc.cursos.service;

import com.duoc.cursos.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public String subirArchivo(
            Long idCurso,
            MultipartFile archivo
    ) {
        validarIdCurso(idCurso);

        String nombreArchivo = obtenerNombreSeguro(archivo);
        String key = construirPrefijo(idCurso) + nombreArchivo;

        guardarArchivo(key, archivo);

        return key;
    }

    public List<Map<String, Object>> listarArchivos(
            Long idCurso
    ) {
        validarIdCurso(idCurso);

        String prefijo = construirPrefijo(idCurso);

        ListObjectsV2Request request =
                ListObjectsV2Request.builder()
                        .bucket(bucketName)
                        .prefix(prefijo)
                        .build();

        return s3Client.listObjectsV2(request)
                .contents()
                .stream()
                .filter(objeto ->
                        !objeto.key().endsWith("/")
                )
                .sorted(Comparator.comparing(S3Object::key))
                .map(this::convertirArchivo)
                .toList();
    }

    public byte[] descargarArchivo(String key) {
        validarKey(key);

        GetObjectRequest request =
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();

        try {
            return s3Client
                    .getObjectAsBytes(request)
                    .asByteArray();

        } catch (S3Exception ex) {
            if (ex.statusCode() == 404) {
                throw new RecursoNoEncontradoException(
                        "No existe el archivo solicitado"
                );
            }

            throw ex;
        }
    }

    public String reemplazarArchivo(
            Long idCurso,
            String key,
            MultipartFile archivo
    ) {
        validarIdCurso(idCurso);
        validarKeyDelCurso(idCurso, key);
        verificarExistencia(key);

        guardarArchivo(key, archivo);

        return key;
    }

    public void eliminarArchivo(
            Long idCurso,
            String key
    ) {
        validarIdCurso(idCurso);
        validarKeyDelCurso(idCurso, key);
        verificarExistencia(key);

        DeleteObjectRequest request =
                DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();

        s3Client.deleteObject(request);
    }

    private void guardarArchivo(
            String key,
            MultipartFile archivo
    ) {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException(
                    "El archivo no puede estar vacío"
            );
        }

        String contentType = archivo.getContentType();

        if (contentType == null || contentType.isBlank()) {
            contentType = "application/octet-stream";
        }

        PutObjectRequest request =
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(contentType)
                        .build();

        try {
            s3Client.putObject(
                    request,
                    RequestBody.fromBytes(archivo.getBytes())
            );

        } catch (IOException ex) {
            throw new IllegalStateException(
                    "No fue posible leer el archivo enviado",
                    ex
            );
        }
    }

    private void verificarExistencia(String key) {
        HeadObjectRequest request =
                HeadObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();

        try {
            s3Client.headObject(request);

        } catch (S3Exception ex) {
            if (ex.statusCode() == 404) {
                throw new RecursoNoEncontradoException(
                        "No existe el archivo solicitado"
                );
            }

            throw ex;
        }
    }

    private Map<String, Object> convertirArchivo(
            S3Object objeto
    ) {
        Map<String, Object> archivo =
                new LinkedHashMap<>();

        String nombre = objeto.key().substring(
                objeto.key().lastIndexOf('/') + 1
        );

        archivo.put("key", objeto.key());
        archivo.put("nombre", nombre);
        archivo.put("tamanoBytes", objeto.size());
        archivo.put(
                "ultimaModificacion",
                objeto.lastModified()
        );

        return archivo;
    }

    private String construirPrefijo(Long idCurso) {
        return "cursos/"
                + idCurso
                + "/materiales/";
    }

    private void validarKeyDelCurso(
            Long idCurso,
            String key
    ) {
        validarKey(key);

        String prefijoEsperado =
                construirPrefijo(idCurso);

        if (!key.startsWith(prefijoEsperado)) {
            throw new IllegalArgumentException(
                    "El archivo no pertenece al curso indicado"
            );
        }
    }

    private void validarKey(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException(
                    "La clave del archivo es obligatoria"
            );
        }
    }

    private void validarIdCurso(Long idCurso) {
        if (idCurso == null || idCurso <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del curso no es válido"
            );
        }
    }

    private String obtenerNombreSeguro(
            MultipartFile archivo
    ) {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException(
                    "El archivo no puede estar vacío"
            );
        }

        String nombre = archivo.getOriginalFilename();

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El archivo no tiene un nombre válido"
            );
        }

        String normalizado =
                nombre.replace('\\', '/');

        String nombreSeguro = normalizado.substring(
                normalizado.lastIndexOf('/') + 1
        );

        if (nombreSeguro.isBlank()
                || nombreSeguro.equals(".")
                || nombreSeguro.equals("..")) {

            throw new IllegalArgumentException(
                    "El archivo no tiene un nombre válido"
            );
        }

        return nombreSeguro;
    }
}