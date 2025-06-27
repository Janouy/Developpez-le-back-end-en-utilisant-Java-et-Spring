package com.openclassrooms.chatopapi;

import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

	private final String uploadsPath = Paths
			.get("C:/Users/jeann/Documents/workspace-spring-tools-for-eclipse-4.30.0.RELEASE/chatopApi/uploads").toUri()
			.toString();

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**").addResourceLocations(uploadsPath).setCachePeriod(3600);
	}
}
