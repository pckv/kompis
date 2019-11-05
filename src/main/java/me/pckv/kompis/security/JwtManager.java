package me.pckv.kompis.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Log
@Component
public class JwtManager {

    private JwtProperties jwtProperties;
    private Key key;

    public JwtManager(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;

        // Create a new unique secret unless a secret is specified in the configuration
        if (jwtProperties.getSecret() == null) {
            this.key = Keys.secretKeyFor(SignatureAlgorithm.forName(jwtProperties.getSecretAlgorithm()));
            log.info("Generated new JWT secret with algorithm " + key.getAlgorithm());
        } else {
            this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
            log.info("Loading secret from configuration using algorithm " + key.getAlgorithm());
        }
    }

    private Date getExpiration() {
        return new Date(System.currentTimeMillis() + jwtProperties.getExpirationDays() * 86400);
    }

    public String generateToken(String subject) {
        return Jwts.builder().setSubject(subject).signWith(key).setExpiration(getExpiration()).compact();
    }

    public String getSubject(String token) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
        } catch (SignatureException e) {
            return null;
        }
    }
}
