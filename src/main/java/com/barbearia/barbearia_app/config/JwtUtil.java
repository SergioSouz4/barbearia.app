package com.barbearia.barbearia_app.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // Extrair o username (email) do token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrair data de expiração do token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extrair claim específico do token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extrair todos os claims do token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Verificar se o token está expirado
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Gerar token para o usuário
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    // Gerar token com claims extras
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return createToken(extraClaims, userDetails.getUsername());
    }

    // Criar o token JWT
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Validar token
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Obter chave de assinatura
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extrair ID do usuário do token (claim customizado)
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    // Extrair tipo de usuário do token (claim customizado)
    public String extractUserType(String token) {
        return extractClaim(token, claims -> claims.get("userType", String.class));
    }

    // Extrair ID do perfil (cliente/barbeiro) do token
    public Long extractProfileId(String token) {
        return extractClaim(token, claims -> claims.get("profileId", Long.class));
    }

    // Gerar token com informações completas do usuário
    public String generateTokenWithUserInfo(UserDetails userDetails, Long userId,
                                            String userType, Long profileId, String nome) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userType", userType);
        claims.put("profileId", profileId);
        claims.put("nome", nome);

        return createToken(claims, userDetails.getUsername());
    }

    // Verificar se o token é válido (sem UserDetails)
    public Boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // Obter tempo restante do token em milissegundos
    public Long getTokenRemainingTime(String token) {
        Date expiration = extractExpiration(token);
        return expiration.getTime() - System.currentTimeMillis();
    }

    // Verificar se o token precisa ser renovado (faltam menos de 1 hora)
    public Boolean shouldTokenBeRefreshed(String token) {
        Long remainingTime = getTokenRemainingTime(token);
        return remainingTime < 3600000; // 1 hora em milissegundos
    }

    // Renovar token (gerar novo com mesmo usuário)
    public String refreshToken(String token) {
        final String username = extractUsername(token);
        final Long userId = extractUserId(token);
        final String userType = extractUserType(token);
        final Long profileId = extractProfileId(token);
        final String nome = extractClaim(token, claims -> claims.get("nome", String.class));

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userType", userType);
        claims.put("profileId", profileId);
        claims.put("nome", nome);

        return createToken(claims, username);
    }
}