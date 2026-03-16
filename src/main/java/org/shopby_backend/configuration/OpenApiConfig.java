package org.shopby_backend.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
    public class OpenApiConfig{
        @Bean
        public OpenAPI shopbyOpenAPI() {
            return new OpenAPI()
                    .info((new Info()
                    .title("Shopby API")
                    .description("API Backend e-commerce développée avec Spring Boot")
                    .version("1.0")
            ));
        }
    }
