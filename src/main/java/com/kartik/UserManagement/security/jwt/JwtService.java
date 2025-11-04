package com.kartik.UserManagement.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

	@Value("${app.jwt.secret}")
	private String secretKey;

    @Value("${app.jwt.expiration-ms}")
    private long expirationTime;
	
	
	public String GenerateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
				.signWith(getKey())
				.compact();
	}

	private Key getKey() {
		byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String extractUserName(String token) {
	    return extractClaim(token, Claims::getSubject);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
	    final Claims claims = extractAllClaims(token);
	    return claimResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
	    return Jwts.parser()
	            .setSigningKey(getKey())
	            .build()
	            .parseSignedClaims(token)
	            .getPayload();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
	    final String userName = extractUserName(token);
	    return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
	    return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
	    return extractClaim(token, Claims::getExpiration);
	}

	
}
