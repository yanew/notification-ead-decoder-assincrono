package com.ead.notification.configs.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtProvider {

	private Logger log = LogManager.getLogger(JwtProvider.class);
	
	@Value("${ead.auth.jwtSecret}")
	private String jwtSecret;
	
	
	public String getSubjectJwt(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
	
	public String getClaimNameJwt(String token, String claimName) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get(claimName).toString();
	}
	
	public boolean validateJwt(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		}catch (SignatureException e) {
			log.error("JWT signature inválida: {}", e.getMessage());
		}catch (MalformedJwtException e) {
			log.error("Token JWT inválido: {}", e.getMessage());
		}catch (ExpiredJwtException e) {
			log.error("Token JWT está expirado: {}", e.getMessage());
		}catch (UnsupportedJwtException e) {
			log.error("Token JWT não é suportado: {}", e.getMessage());
		}catch (IllegalArgumentException e) {
			log.error("JWT claims string está vazia: {}", e.getMessage());
		}
		return false;
	}

	
}
