package kz.brdevelopment.test.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.access_secret}")
    private String ACCESS_SECRET;

    @Value("${jwt.refresh_secret}")
    private String REFRESH_SECRET;

    private static final long EXPIRATION = 86400000; // 1 день

    public String generateToken(Long userId, int tokenVersion) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION);
        System.out.println("tokenVersion: " + tokenVersion);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("userId", userId)
                .claim("tokenVersion", tokenVersion)
                .setIssuedAt(now)
               // .setExpiration(expiry)
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 минута
                .signWith(Keys.hmacShaKeyFor(ACCESS_SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(ACCESS_SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Claims verifyAccessToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(ACCESS_SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long extractUserId(String token) {
        return getAccessClaims(token).get("userId", Long.class);
    }

    public Integer extractTokenVersion(String token) {
        return getAccessClaims(token).get("tokenVersion", Integer.class);
    }

    private Claims getAccessClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(ACCESS_SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(ACCESS_SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw e; // пробрасываем, чтобы отловить в фильтре
        } catch (JwtException e) {
            return false;
        }
    }
}
