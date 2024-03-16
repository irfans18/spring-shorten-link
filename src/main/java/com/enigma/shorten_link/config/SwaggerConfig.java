package com.enigma.shorten_link.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(
        title = "Spring Shorten Link API",
        version = "1.0.0",
        description = "Spring Shorten Link API"
))
@SecurityScheme(name = "Authorization", scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT")
@Configuration
public class SwaggerConfig {
}
