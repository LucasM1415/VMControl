package com.example.vmcontrol.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI vmControlOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("VMControl API")
                        .description("Sistema de Gerenciamento de Máquinas Virtuais")
                        .version("1.0.0"));
    }
}