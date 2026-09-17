package com.macias34.codemastery.security.jwt;

import java.util.Date;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.macias34.codemastery.security.constants.SecurityConstants;
import com.macias34.codemastery.user.entity.UserRole;
import com.macias34.codemastery.user.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtGenerator {

	UserService userService;

	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		UserRole userRole = userService.getUserByUsername(username).getRole();

		Date currentDate = new Date();
		Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

		String token = Jwts.builder()
				.setSubject(username)
				.claim("role", userRole)
				.setIssuedAt(currentDate)
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET)
				.compact();

		return token;
	}

	public String getUsernameFromJWT(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(SecurityConstants.JWT_SECRET).parseClaimsJws(token).getBody();

		return claims.getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser()
					.setSigningKey(SecurityConstants.JWT_SECRET).parseClaimsJws(token);

			return true;
		}

		catch (Exception exception) {
			System.out.println(exception.getMessage());
			throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect.");
		}
	}
}
