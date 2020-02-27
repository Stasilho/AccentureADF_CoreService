package com.accenture.com.snoskov.adf.universityx.security.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    private static final String AUTHORITY_KEY = "role";

    private Environment env;

    @Autowired
    public JwtTokenUtil(Environment env) {
        this.env = env;
    }

    public String generateToken(String username, String authority) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITY_KEY, authority);
        return generateToken(claims, username);
    }

    private String generateToken(Map<String, Object> claims, String subject) {
        String secret = env.getProperty("universityx.api.auth.secret");
        long expirationPeriodMsec = Long.valueOf(env.getProperty("universityx.api.auth.expiration.days")) * 24 * 3600 * 1000;
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationPeriodMsec))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        final Claims claims = extractAllClaims(token);
        final String username = claims.getSubject();
        final String authority = claims.get(AUTHORITY_KEY).toString();
        final Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(authority));

        return new UsernamePasswordAuthenticationToken(username, "", authorities);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        String secret = env.getProperty("universityx.api.auth.secret");
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
