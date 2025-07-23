package com.spendless.auth.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTService {

   private final String secret = "my-secret-key-sqidfwsz2e1298-ewkfjnb93fi3-tokendwd23-fknqewf";
   private final SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());
   private final long  acceesTokenExpireTime = 1000*60*60*24;
   private final long  refreshTokenExpireTime = 1000*60*60*24*30;


    public String generateAccessToken(String username){
       String accessToken =  Jwts.builder()
                .setSubject(username)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ acceesTokenExpireTime))
                .compact();
        return accessToken;
    }

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }


    public  String generateRefreshToken(String username){
        String accessToken =  Jwts.builder()
                .setSubject(username)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ refreshTokenExpireTime))
                .compact();
        return accessToken;
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }




}
