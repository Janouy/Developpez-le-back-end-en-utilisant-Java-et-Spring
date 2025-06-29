package com.openclassrooms.chatopapi.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import com.openclassrooms.chatopapi.model.User;
import com.openclassrooms.chatopapi.repository.UserRepository;

@Service
public class JwtService {

	private JwtEncoder jwtEncoder;
	private final JwtDecoder jwtDecoder;

	@Autowired
	private UserRepository userRepository;

	public JwtService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, UserRepository userRepository) {
		this.jwtEncoder = jwtEncoder;
		this.jwtDecoder = jwtDecoder;
		this.userRepository = userRepository;
	}

	public String generateToken(User user) {
		Instant now = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder().issuer("self").issuedAt(now)
				.expiresAt(now.plus(1, ChronoUnit.DAYS)).claim("userId", user.getId()).build();

		JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
				.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
		return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
	}

	public Authentication getAuthentication(String token) {
		Jwt jwt = jwtDecoder.decode(token);
		Integer userId = ((Number) jwt.getClaim("userId")).intValue();
		User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		List<GrantedAuthority> authorities = Collections.emptyList();
		return new UsernamePasswordAuthenticationToken(user, null, authorities);
	}

	public boolean validateToken(String token) {
		try {
			Jwt jwt = jwtDecoder.decode(token);
			Instant expiresAt = jwt.getExpiresAt();
			return expiresAt != null && Instant.now().isBefore(expiresAt);
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

}
