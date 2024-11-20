package org.example.expert.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // JWT 토큰 생성
    public String createToken(
            String userId,
            String email,
            String userRole,
            String nickname) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("email", email);
        claims.put("userRole", userRole);
        claims.put("nickname", nickname);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))  // 1일 만료
                .signWith(secretKey)
                .compact();
    }

    // 토큰에서 클레임 추출
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            extractClaims(token);  // 유효성 검사
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // 만료된 토큰
            return false;
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            // 지원되지 않는 토큰
            return false;
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            // 잘못된 토큰
            return false;
        } catch (Exception e) {
            // 그 외의 예외
            return false;
        }
    }

    // Authorization 헤더에서 Bearer 토큰 추출
    public String substringToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);  // "Bearer " 부분을 제외하고 반환
        }
        return null;
    }
}
