package org.example.cropmonitoringsystembackend.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.example.cropmonitoringsystembackend.service.JWTService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JWTServiceIMPL implements JWTService {
    @Value("${token.key}")
    String jwtKey;

    @Override
    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities());
        Date currentDate = new Date();
        Date expiredDate = new Date(currentDate.getTime() + 1000 * 60 * 1); //1 minit
        String accessToken = Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername()) // set the "subject" (sub) claim of the JWT
                .setIssuedAt(currentDate) // set the "issued at" (iat) claim of the JWT
                .setExpiration(expiredDate) // set the "expiration" (exp) claim of the JWT
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // give the signing key and the signing
                // algorithm for signing the JWT
                .compact();

        return accessToken;
    }

//    @Override
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        String subject = extractClaims(token, Claims::getSubject);
//        return subject.equals(userDetails.getUsername()) && !isExpired(token);
//    }
@Override
public boolean isTokenValid(String token, UserDetails userDetails) {
    try {
        String subject = extractClaims(token, Claims::getSubject);
        return subject.equals(userDetails.getUsername()) && !isExpired(token);
    } catch (io.jsonwebtoken.ExpiredJwtException e) {
        // Token is expired
        return false;
    }
}


    private Key getSignKey() {
        byte[] bytes = Decoders.BASE64.decode(jwtKey); //decode the base64-encoded jwt
        return Keys.hmacShaKeyFor(bytes); // generate an HMAC (Hash-based Message Authentication Code) signing key
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimResolve) {
        Claims claims = getAllClaims(token);
        return claimResolve.apply(claims);
    }

    private boolean isExpired(String token) {
        Date expiredDate = extractClaims(token, Claims::getExpiration);
        return expiredDate.before(new Date());
    }
}

