-- =========================================================
-- Plataforma de cursos Cloud Native
-- Script 02: datos iniciales
-- =========================================================

INSERT INTO CURSOS (
    NOMBRE,
    DESCRIPCION,
    INSTRUCTOR,
    CUPO_MAXIMO,
    ACTIVO
) VALUES (
    'Desarrollo Cloud Native',
    'Curso de microservicios, contenedores y servicios cloud.',
    'Pedro Breit',
    30,
    1
);

INSERT INTO CURSOS (
    NOMBRE,
    DESCRIPCION,
    INSTRUCTOR,
    CUPO_MAXIMO,
    ACTIVO
) VALUES (
    'Spring Boot y Microservicios',
    'Construcción de APIs REST utilizando Spring Boot.',
    'Ana Torres',
    25,
    1
);

INSERT INTO CURSOS (
    NOMBRE,
    DESCRIPCION,
    INSTRUCTOR,
    CUPO_MAXIMO,
    ACTIVO
) VALUES (
    'Mensajería con RabbitMQ',
    'Procesamiento asíncrono mediante productores y consumidores.',
    'Carlos Soto',
    20,
    1
);

COMMIT;