package com.isil.edu.pe.spedidos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .authorizeHttpRequests(auth -> auth

                        // Recursos publicos
                        .requestMatchers("/assets/**")
                        .permitAll()

                        // Consola H2 deshabilitada
                        .requestMatchers("/h2-console/**")
                        .denyAll()

                        // Solo OPERADOR puede registrar pedidos
                        .requestMatchers(
                                HttpMethod.POST,
                                "/pedidos"
                        )
                        .hasRole("OPERADOR")

                        // Reportes REST: OPERADOR y CONSULTA
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/pedidos",
                                "/api/v1/pedidos/**"
                        )
                        .hasAnyRole("OPERADOR", "CONSULTA")

                        // Vista JSP de reportes PA3
                        .requestMatchers(
                                HttpMethod.GET,
                                "/reportes/pedidos"
                        )
                        .hasAnyRole("OPERADOR", "CONSULTA")

                        // Vista principal de pedidos
                        .requestMatchers(
                                HttpMethod.GET,
                                "/pedidos"
                        )
                        .hasAnyRole("OPERADOR", "CONSULTA")

                        // Cualquier otra solicitud requiere login
                        .anyRequest()
                        .authenticated()
                )

                // Formulario de inicio de sesion
                .formLogin(Customizer.withDefaults())

                // Cierre de sesion
                .logout(Customizer.withDefaults())

                // CSRF permanece habilitado por defecto
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder encoder,

            @Value("${demo.security.operador-password}")
            String operadorPassword,

            @Value("${demo.security.consulta-password}")
            String consultaPassword) {

        // Usuario con permisos de registro y consulta
        UserDetails operador = User
                .withUsername("operador")
                .password(encoder.encode(operadorPassword))
                .roles("OPERADOR")
                .build();

        // Usuario con permisos de consulta
        UserDetails consulta = User
                .withUsername("consulta")
                .password(encoder.encode(consultaPassword))
                .roles("CONSULTA")
                .build();

        return new InMemoryUserDetailsManager(
                operador,
                consulta
        );
    }
}
