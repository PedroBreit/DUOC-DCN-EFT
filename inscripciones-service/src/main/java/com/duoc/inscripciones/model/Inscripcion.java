package com.duoc.inscripciones.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "INSCRIPCIONES",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_INSCRIPCION_ESTUDIANTE_CURSO",
                        columnNames = {"ID_ESTUDIANTE", "ID_CURSO"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INSCRIPCION")
    private Long id;

    @Column(name = "ID_ESTUDIANTE", nullable = false)
    private Long idEstudiante;

    @Column(name = "NOMBRE_ESTUDIANTE", nullable = false, length = 120)
    private String nombreEstudiante;

    @Column(name = "CORREO_ESTUDIANTE", nullable = false, length = 150)
    private String correoEstudiante;

    @Column(name = "ID_CURSO", nullable = false)
    private Long idCurso;

    @Column(name = "ESTADO", nullable = false, length = 30)
    private String estado;

    @Column(name = "FECHA_INSCRIPCION", nullable = false)
    private LocalDateTime fechaInscripcion;

    @PrePersist
    public void prePersist() {
        if (estado == null || estado.isBlank()) {
            estado = "CONFIRMADA";
        }

        if (fechaInscripcion == null) {
            fechaInscripcion = LocalDateTime.now();
        }
    }
}