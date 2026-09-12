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
                        .description("""
                                Spring Boot REST API powering Merimna, a full-stack application
                                for managing supported living services for people with disabilities.
                                """)
                );
    }
}
