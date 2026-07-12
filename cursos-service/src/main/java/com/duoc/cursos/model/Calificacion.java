package com.duoc.cursos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "CALIFICACIONES",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_CALIF_EXAMEN_EST",
                        columnNames = {
                                "ID_EXAMEN",
                                "ID_ESTUDIANTE"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CALIFICACION")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_EXAMEN", nullable = false)
    private Examen examen;

    @Column(name = "ID_ESTUDIANTE", nullable = false)
    private Long idEstudiante;

    @Column(
            name = "RESPUESTA_ESTUDIANTE",
            nullable = false,
            length = 1000
    )
    private String respuestaEstudiante;

    @Column(
            name = "PUNTAJE",
            nullable = false,
            precision = 5,
            scale = 2
    )
    private BigDecimal puntaje;

    @Column(name = "FECHA_RESPUESTA", nullable = false)
    private LocalDateTime fechaRespuesta;

    @PrePersist
    public void prePersist() {
        if (puntaje == null) {
            puntaje = BigDecimal.ZERO;
        }

        if (fechaRespuesta == null) {
            fechaRespuesta = LocalDateTime.now();
        }
    }
}