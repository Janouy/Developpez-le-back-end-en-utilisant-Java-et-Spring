package com.openclassrooms.chatopapi;

import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring configuration class for serving static resources.
 *
 * This class implements {@link WebMvcConfigurer} to define a custom resource
 * handler. It maps all requests to "/uploads/**" to the local "./uploads"
 * directory.
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

	private final String uploadsPath = Paths.get("./uploads").toUri().toString();

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**").addResourceLocations(uploadsPath).setCachePeriod(3600);
	}
}
