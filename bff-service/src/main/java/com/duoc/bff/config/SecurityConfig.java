package com.duoc.bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("!test")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // =================================================
                        // ARCHIVOS
                        // =================================================

                        // Solo INSTRUCTOR puede subir materiales
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/plataforma/cursos/*/archivos"
                        ).hasRole("INSTRUCTOR")

                        // Solo INSTRUCTOR puede reemplazar materiales
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/plataforma/cursos/*/archivos"
                        ).hasRole("INSTRUCTOR")

                        // Solo INSTRUCTOR puede eliminar materiales
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/plataforma/cursos/*/archivos"
                        ).hasRole("INSTRUCTOR")

                        // Ambos roles pueden descargar materiales
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/plataforma/archivos"
                        ).hasAnyRole(
                                "ESTUDIANTE",
                                "INSTRUCTOR"
                        )

                        // =================================================
                        // CURSOS
                        // =================================================

                        // Solo INSTRUCTOR puede crear cursos
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/plataforma/cursos"
                        ).hasRole("INSTRUCTOR")

                        // Solo INSTRUCTOR puede editar cursos
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/plataforma/cursos/*"
                        ).hasRole("INSTRUCTOR")

                        // Solo INSTRUCTOR puede eliminar cursos
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/plataforma/cursos/*"
                        ).hasRole("INSTRUCTOR")

                        // =================================================
                        // EXÁMENES
                        // =================================================

                        // Solo INSTRUCTOR puede crear exámenes
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/plataforma/cursos/*/examenes"
                        ).hasRole("INSTRUCTOR")

                        // Solo INSTRUCTOR puede editar exámenes
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/plataforma/examenes/*"
                        ).hasRole("INSTRUCTOR")

                        // Solo INSTRUCTOR puede eliminar exámenes
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/plataforma/examenes/*"
                        ).hasRole("INSTRUCTOR")

                        // Solo ESTUDIANTE puede responder exámenes
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/plataforma/examenes/*/respuestas"
                        ).hasRole("ESTUDIANTE")

                        // Ambos roles pueden consultar un examen
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/plataforma/examenes/*"
                        ).hasAnyRole(
                                "ESTUDIANTE",
                                "INSTRUCTOR"
                        )

                        // =================================================
                        // CALIFICACIONES
                        // =================================================

                        // Solo INSTRUCTOR puede listar calificaciones
                        // de un curso
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/plataforma/cursos/*/calificaciones"
                        ).hasRole("INSTRUCTOR")

                        // Solo INSTRUCTOR puede modificar puntajes
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/plataforma/calificaciones/*"
                        ).hasRole("INSTRUCTOR")

                        // Solo ESTUDIANTE puede consultar sus resultados
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/plataforma/calificaciones/"
                                        + "estudiantes/*"
                        ).hasRole("ESTUDIANTE")

                        // =================================================
                        // CONSULTAS DE CURSOS
                        // =================================================

                        // Ambos roles pueden listar y consultar cursos,
                        // materiales y exámenes
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/plataforma/cursos",
                                "/api/plataforma/cursos/*",
                                "/api/plataforma/cursos/*/archivos",
                                "/api/plataforma/cursos/*/examenes"
                        ).hasAnyRole(
                                "ESTUDIANTE",
                                "INSTRUCTOR"
                        )

                        // =================================================
                        // INSCRIPCIONES Y RABBITMQ
                        // =================================================

                        // Solo INSTRUCTOR consume mensajes de la cola de errores
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/plataforma/inscripciones/"
                                        + "mensajes/errores/consumir"
                        ).hasRole("INSTRUCTOR")

                        // Solo ESTUDIANTE gestiona inscripciones
                        .requestMatchers(
                                "/api/plataforma/inscripciones/**"
                        ).hasRole("ESTUDIANTE")

                        // Todo lo demás queda bloqueado
                        .anyRequest().denyAll()
                )

                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(
                                        jwtAuthenticationConverter()
                                )
                        )
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter
    jwtAuthenticationConverter() {

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(
                new JwtRoleConverter()
        );

        converter.setPrincipalClaimName("sub");

        return converter;
    }
}