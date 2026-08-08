package com.project.ewallet.user;

import com.project.ewallet.user.dto.JwtClaimsDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret:your-secret-key-must-be-at-least-256-bits-long-for-HS256-algorithm-use}")
    private String jwtSecret;

    @Value("${jwt.access-token-expiration:5000}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration:2592000000}")
    private long refreshTokenExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateAccessToken(JwtClaimsDto claims) {
        return generateToken(claims, accessTokenExpiration);
    }

    public String generateRefreshToken(JwtClaimsDto claims) {
        return generateToken(claims, refreshTokenExpiration);
    }

    private String generateToken(JwtClaimsDto claims, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(claims.getUsername())
                .claim("userId", claims.getUserId())
                .claim("email", claims.getEmail())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public JwtClaimsDto validateTokenAndGetClaims(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return new JwtClaimsDto(
                    claims.get("userId", Long.class),
                    claims.getSubject(),
                    claims.get("email", String.class)
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid token: " + e.getMessage());
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}

