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
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Endpoints públicos
                        .requestMatchers(
                                "/actuator/health",
                                "/actuator/info"
                        ).permitAll()

                        // Solo INSTRUCTOR puede subir materiales
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/plataforma/cursos/*/archivos"
                        ).hasRole("INSTRUCTOR")

                        // ESTUDIANTE e INSTRUCTOR pueden descargar materiales
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/plataforma/archivos"
                        ).hasAnyRole(
                                "ESTUDIANTE",
                                "INSTRUCTOR"
                        )

                        // Consulta de cursos
                        .requestMatchers(
                                "/api/plataforma/cursos/**"
                        ).hasAnyRole(
                                "ESTUDIANTE",
                                "INSTRUCTOR"
                        )

                        // Inscripciones
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
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(
                new JwtRoleConverter()
        );

        converter.setPrincipalClaimName("sub");

        return converter;
    }
}