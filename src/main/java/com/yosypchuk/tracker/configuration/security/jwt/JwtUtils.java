package com.yosypchuk.tracker.configuration.security.jwt;

import com.yosypchuk.tracker.configuration.security.UsersDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@AllArgsConstructor
@PropertySource("classpath:application.yml")
public class JwtUtils {
    private static String secret;
    private static int tokenExpirationMs;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        JwtUtils.secret = secret;
    }

    @Value("${jwt.expired}")
    public void setValidity(int validity) {
        JwtUtils.tokenExpirationMs = validity;
    }

    public static String generateToken(Authentication authentication) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        UsersDetailsImpl user = (UsersDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("authorities", authentication.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(tokenExpirationMs)))
                .signWith(secretKey)
                .compact();
    }

    public static Jws<Claims> getUsernameFromToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        JwtParser parser = Jwts.parserBuilder().setSigningKey(secretKey).build();

        return parser.parseClaimsJws(token);
    }

    public static Authentication mapTokenToAuthentication(String token) {
        Jws<Claims> jws = getUsernameFromToken(token);
        Claims body = jws.getBody();

        String username = body.getSubject();
        List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");

        List<GrantedAuthority> grantedAuthorities = authorities.stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
    }
}
