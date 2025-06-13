package com.openclassrooms.chatopapi.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;


import org.springframework.stereotype.Service;


import com.openclassrooms.chatopapi.model.User;

@Service
public class JwtService {
	
	private JwtEncoder jwtEncoder;
	private final JwtDecoder jwtDecoder;
	
	public JwtService (JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
		this.jwtEncoder = jwtEncoder;
		this.jwtDecoder = jwtDecoder;
	}
	

	public String generateToken(User user) {
	       Instant now = Instant.now();
	       JwtClaimsSet claims = JwtClaimsSet.builder()
	    		   .issuer("self")
	    		   .issuedAt(now)
	    		   .expiresAt(now.plus(1,ChronoUnit.DAYS))
	    		   .subject(user.getEmail())
	    		   .build();
	       
	       JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
	       return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
	}
	
	  public Jwt decodeToken(String token) {
	        return jwtDecoder.decode(token);
	    }

	    public String extractEmail(String token) {
	        return decodeToken(token).getSubject();
	    }
}
