package com.sample.school.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

public class SecurityUtil {
    private static final Key key = Jwts.SIG.HS256.key().build();

    private static final long EXPIRATION_MS = 3600000; // 1 hour

    public static String genToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key)
                .compact();
    }

    public static String validateToken(String token) {
        try {
            return Jwts.parser().verifyWith((SecretKey)key).build().parseSignedClaims(token).getPayload().getSubject();
        } catch (JwtException e) {
            return null;
        }
    }
}
