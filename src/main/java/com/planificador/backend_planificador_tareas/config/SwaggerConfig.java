package com.planificador.backend_planificador_tareas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Planificador de Tareas")
                        .version("1.0.0")
                        .description("Documentación de los endpoints REST para el gestor de tareas (Sprint 4)")
                        .contact(new Contact()
                                .name("Equipo Backend")
                                .email("soporte@planificador.com")));
    }
}