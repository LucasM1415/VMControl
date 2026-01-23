package com.example.vmcontrol.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI vmControlOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("VMControl API")
                        .description("Sistema de Gerenciamento de Máquinas Virtuais")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe VMControl")
                                .email("suporte@vmcontrol.com")
                                .url("https://vmcontrol.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"));
    }
}