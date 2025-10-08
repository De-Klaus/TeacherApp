package org.teacher.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.teacher.dto.JwtAuthenticationDto;
import org.teacher.entity.Role;
import org.teacher.entity.User;
import org.teacher.exception.InvalidJwtTokenException;

import javax.crypto.SecretKey;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private static final Logger LOGGER = LogManager.getLogger(JwtService.class);
    @Value("8074658237c236e39e96e909ac1abb25a3e1773b100096ad6877c439cd452c17")
    private String jwtSecret;

    public JwtAuthenticationDto generateAuthToken(User user) {
        return new JwtAuthenticationDto(
                generateJwtToken(user),
                generateRefreshToken(user)
        );
    }

    public JwtAuthenticationDto refreshBaseToken(User user, String refreshToken) {
        return new JwtAuthenticationDto(
                generateJwtToken(user),
                refreshToken
        );
    }

    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSingInKey())
                    .build()
                    .parseSignedClaims(token) // парсим JWS (signed JWT)
                    .getPayload(); // получаем payload (claims)
            return claims.get("email", String.class);
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtTokenException(e);
        }
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSingInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        }catch (ExpiredJwtException expEx){
            LOGGER.error("Expired JwtException", expEx);
        }catch (UnsupportedJwtException expEx){
            LOGGER.error("Unsupported JwtException", expEx);
        }catch (MalformedJwtException expEx){
            LOGGER.error("Malformed JwtException", expEx);
        }catch (SecurityException expEx){
            LOGGER.error("Security Exception", expEx);
        }catch (Exception expEx){
            LOGGER.error("invalid token", expEx);
        }
        return false;
    }

    private String generateJwtToken(User user) {
        Date date = Date.from(LocalDateTime.now().plusMinutes(60).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(String.valueOf(user.getUserId()))
                .expiration(date) // token validity period
                .claims(getUserClaims(user)) //check
                .signWith(getSingInKey())
                .compact();
    }

    private String generateRefreshToken(User user) {
        Date date = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(String.valueOf(user.getUserId()))
                .expiration(date)
                .claims(getUserClaims(user))
                .signWith(getSingInKey())
                .compact();
    }

    private static Map<String, Object> getUserClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles().stream().map(Role::name).toList());
        return claims;
    }

    private SecretKey getSingInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}