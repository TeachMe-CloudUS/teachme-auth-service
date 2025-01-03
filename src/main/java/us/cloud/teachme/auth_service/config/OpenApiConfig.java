package us.cloud.teachme.auth_service.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

  @Value("${base-url}")
  private String BASE_URL;

  @Bean
  OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(new Components().addSecuritySchemes("bearer-key", new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")))
        .servers(List.of(new Server().url(BASE_URL)))
        .info(new Info().title("Teachme Auth Service API").version("1.0"));
  }

}
