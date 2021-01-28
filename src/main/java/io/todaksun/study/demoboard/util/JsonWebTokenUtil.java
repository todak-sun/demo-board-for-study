package io.todaksun.study.demoboard.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class JsonWebTokenUtil {

    private final String VERY_SECRET_KEY = "guess";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public LocalDateTime extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractClaims(token);
        return resolver.apply(claims);
    }

    public boolean isValidToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        log.info("username : {}", username);
        log.info("userDetails.getUsername() : {}", userDetails.getUsername());
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String createToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        final long now = Timestamp.valueOf(LocalDateTime.now()).getTime();
        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 30))
                .signWith(SignatureAlgorithm.HS256, VERY_SECRET_KEY)
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).isBefore(LocalDateTime.now());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(VERY_SECRET_KEY).parseClaimsJws(token).getBody();
    }

}
