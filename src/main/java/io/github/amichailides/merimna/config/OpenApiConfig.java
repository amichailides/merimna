package io.github.amichailides.merimna.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI merimnaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Merimna API")
                        .version("v1")
                        .description("Merimna (Μέριμνα - Greek for \"Care\") is a Spring Boot REST API for managing supported living structures for people with disabilities.")
                );
    }
}