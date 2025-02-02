package us.cloud.teachme.auth_service.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import us.cloud.teachme.auth_service.model.User;

@Data
@Service
public class JwtService {

  @Value("${jwt.secret-key}")
  private String SECRET_KEY;

  @Value("${jwt.expiration-time}")
  private long EXPIRATION_TIME;

  public String generateToken(User user) {
    long actualTime = System.currentTimeMillis();
    return Jwts.builder()
        .subject(user.getId())
        .claim("role", user.getRole())
        .claim("enabled", user.isEnabled())
        .issuedAt(new Date(actualTime))
        .expiration(new Date(actualTime + EXPIRATION_TIME))
        .signWith(getSignKey())
        .compact();
  }

  public void validateToken(String token) {
    Jwts.parser().verifyWith(getSignKey()).build().parse(token).getPayload();
  }

  public Claims extractToken(String token) {
    return Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).getPayload();
  }

  private SecretKey getSignKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
  }

}
