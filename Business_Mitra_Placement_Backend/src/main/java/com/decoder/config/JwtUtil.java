package com.decoder.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String jwtSecret = "secretKeyExampleSecretKeyExample"; // Must be at least 32 bytes for HS256
    private final long jwtExpirationMs = 86400000;

    private final Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

    // Generate token using email (username)
    public String generateToken(String email, String userType) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userType", userType)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
    public String extractUserType(String token) {
        return extractClaim(token, claims -> claims.get("userType", String.class));
    }


    // Extract username/email from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Validate token using user details
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Check if token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Get token expiration date
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract a claim using a resolver
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
