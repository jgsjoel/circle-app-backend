package com.chat.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("${jwt.exp.time}")
    private long EXPIRATION_TIME;
    @Value("${jwt.issuer}")
    private String issuer;
    private SecretKey secretKey;

    @PostConstruct
    public void init(){
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    public String generateToken(String subject) {
        return Jwts
                .builder()
                .issuer(issuer)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusSeconds(EXPIRATION_TIME)))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (SignatureException | MalformedJwtException |
                 ExpiredJwtException | UnsupportedJwtException |
                 IllegalArgumentException e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    public Date getExpiration(String token) {
        return getClaims(token).getExpiration();
    }
}

