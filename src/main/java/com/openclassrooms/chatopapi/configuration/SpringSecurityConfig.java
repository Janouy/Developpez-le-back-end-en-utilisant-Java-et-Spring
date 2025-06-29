package com.openclassrooms.chatopapi.configuration;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.openclassrooms.chatopapi.security.JwtAuthenticationFilter;
import com.openclassrooms.chatopapi.security.RestAuthenticationEntryPoint;
import com.openclassrooms.chatopapi.service.JwtService;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

	@Value("${JWT_KEY}")
	private String jwtKey;

	/**
	 * Defines the security filter chain for public endpoints
	 */
	@Bean
	@Order(1)
	public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
		return http
				.securityMatchers(m -> m.requestMatchers("/auth/login", "/auth/register", "/uploads/**",
						"/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html"))
				.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(a -> a.anyRequest().permitAll()).build();
	}

	/**
	 * Defines the security filter chain for protected endpoints. Uses JWT-based
	 * authentication, and applies a custom authentication entry point.
	 */
	@Bean
	@Order(2)
	public SecurityFilterChain protectedFilterChain(HttpSecurity http, JwtService jwtService,
			RestAuthenticationEntryPoint entryPoint) throws Exception {
		return http.securityMatchers(m -> m.requestMatchers("/**")).cors(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(new JwtAuthenticationFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint))
				.authorizeHttpRequests(a -> a.anyRequest().authenticated()).oauth2ResourceServer(
						oauth2 -> oauth2.authenticationEntryPoint(entryPoint).jwt(Customizer.withDefaults()))
				.build();
	}

	/**
	 * Provides a JWT encoder using the configured secret key.
	 */
	@Bean
	public JwtEncoder jwtEncoder() {
		return new NimbusJwtEncoder(new ImmutableSecret<>(jwtKey.getBytes()));
	}

	/**
	 * Provides a JWT decoder using the configured secret key
	 */
	@Bean
	public JwtDecoder jwtDecoder() {
		SecretKeySpec secretKey = new SecretKeySpec(jwtKey.getBytes(), "HmacSHA256");
		return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
	}

	/**
	 * Provides a BCrypt password encoder for secure password hashing.
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}