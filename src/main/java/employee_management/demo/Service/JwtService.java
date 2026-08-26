package employee_management.demo.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${refresh-token.secret:${jwt.secret}}")
    private String refreshSecret;

    @Value("${refresh-token.expiration}")
    private long refreshExpiration;
 // creates signature  with HS256 +secret key
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(String email,String Type) {

        return Jwts.builder()
                .subject(email)
                .claim("type", Type)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .subject(email)
                .claim("type", "REFRESH")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(getRefreshSigningKey())
                .compact();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(
            String token,
            String email,
            String expectedType
    ) {
        try {
            Claims claims = extractAllClaims(token, signingKeyFor(expectedType));
            return email.equals(claims.getSubject())
                    && expectedType.equals(claims.get("type", String.class))
                    && claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }
    public String extractType(String token) {
        return extractAllClaims(token)
                .get("type", String.class);
    }

    public String extractRefreshEmail(String token) {
        return extractAllClaims(token, getRefreshSigningKey()).getSubject();
    }

 // checks is token expired or not by passing token
    public boolean isTokenExpired(String token) {

        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return extractAllClaims(token, getSigningKey());
    }

    private Claims extractAllClaims(String token, SecretKey signingKey) {

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getRefreshSigningKey() {
        return Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey signingKeyFor(String tokenType) {
        return "REFRESH".equals(tokenType) ? getRefreshSigningKey() : getSigningKey();
    }
}
