package com.ruco.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ruco.exception.JwtException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Service
public class JwtService {

	@Value("${jwt.secret-key}")
	private String SECRET;
	private final long EXPIRATION = 24 * 60 * 60 * 1000; // 24 hr

	public String validateToken(String token) throws JwtException {

		try {
			Jws<Claims> claims = Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token);
			return claims.getPayload().getSubject();
		}

		catch (ExpiredJwtException e) {
			throw new JwtException("Token Expired");
		}

		catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
			throw new JwtException("Invalid Token");
		}
	}

	private SecretKey getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		SecretKey key = Keys.hmacShaKeyFor(keyBytes);
		return key;
	}

	private String createToken(Map<String, Object> claims, String code) {
		String token = Jwts.builder().claims(claims).subject(code).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + EXPIRATION)).signWith(getSignKey()).compact();
		return token;
	}

	public String generate(String code) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, code);
	}

}