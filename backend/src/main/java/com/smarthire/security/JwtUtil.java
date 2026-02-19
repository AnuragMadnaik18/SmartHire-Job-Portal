package com.smarthire.security;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

    @Value("${jwt.token.expiration.millis}")
    private long jwtExpiration;

    @Value("${jwt.token.secret}")
    private String jwtSecret;

    private Key jwtKey;

    @PostConstruct
    public void init() {
        jwtKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String createToken(Authentication auth) {
        UserDetails user = (UserDetails) auth.getPrincipal();

        String roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(user.getUsername()) // email
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .claim("role", roles)
                .signWith(jwtKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication validateToken(String token) {

        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(jwtKey)
                .build();

        Claims claims = parser
                .parseClaimsJws(token)
                .getBody();

        String email = claims.getSubject();
        String roles = (String) claims.get("role");

        List<GrantedAuthority> authorities =
                AuthorityUtils.commaSeparatedStringToAuthorityList(roles);

        return new UsernamePasswordAuthenticationToken(email, null, authorities);
    }
}
