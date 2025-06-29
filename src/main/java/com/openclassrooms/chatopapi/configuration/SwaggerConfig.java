package com.openclassrooms.chatopapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Swagger/OpenAPI configuration
 *
 * This configuration sets up the OpenAPI documentation using springdoc-openapi.
 * It includes: - A security scheme definition using HTTP bearer authentication
 * with JWT. - A global security requirement that applies the bearer token to
 * all API endpoints. - Basic API information such as the title and version.
 *
 * This setup allows tools like Swagger UI to understand how to authorize
 * requests using JWT tokens and provides a structured API documentation.
 */
@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.components(new Components().addSecuritySchemes("bearerAuth",
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
				.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
				.info(new Info().title("Chatop API").version("1.0"));
	}
}
