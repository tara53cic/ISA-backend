package isa.jutjubic.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import isa.jutjubic.model.User;

import javax.crypto.SecretKey;

@Component
public class TokenUtils {

	private final String SECRET = "somesecret-key-for-jwt-token-has-to-be-512-bits-long-1234567890123456789";
	private final int EXPIRES_IN = 1800000; // 30 min

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
	}

	public int getExpiredIn() {
		return EXPIRES_IN;
	}

	// Generate token
	public String generateToken(String username) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + EXPIRES_IN);

		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(now)
				.setExpiration(expiry)
				.signWith(getSigningKey(), SignatureAlgorithm.HS512)
				.compact();
	}

	// Get username from token
	public String getUsernameFromToken(String token) {
		try {
			Claims claims = Jwts.parserBuilder()
					.setSigningKey(getSigningKey())
					.build()
					.parseClaimsJws(token)
					.getBody();
			return claims.getSubject();
		} catch (Exception e) {
			return null;
		}
	}

	// Validate token
	public boolean validateToken(String token, UserDetails userDetails) {
		String username = getUsernameFromToken(token);
		if (username == null || !username.equals(userDetails.getUsername())) {
			return false;
		}

		try {
			Claims claims = Jwts.parserBuilder()
					.setSigningKey(getSigningKey())
					.build()
					.parseClaimsJws(token)
					.getBody();

			Date expiration = claims.getExpiration();
			if (expiration.before(new Date())) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}
}
