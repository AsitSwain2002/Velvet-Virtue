package com.org.Velvet.Virtue.service.impl;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import com.org.Velvet.Virtue.ExceptionHandler.InvalidJwtToken;
import com.org.Velvet.Virtue.ExceptionHandler.JwtTokenExpaired;
import com.org.Velvet.Virtue.Model.Users;
import com.org.Velvet.Virtue.service.JwtService;

@Service
public class JwtServImpl implements JwtService {

	private String token = "";

	public JwtServImpl() {
		try {
			KeyGenerator instance = KeyGenerator.getInstance("HmacSHA256");
			SecretKey generateKey = instance.generateKey();
			token = Base64.getEncoder().encodeToString(generateKey.getEncoded());
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public String generateTooken(Users users) {
		Map<String, Object> claim = new HashMap<String, Object>();
		claim.put("role", users.getRoles());
		return Jwts.builder().claims().add(claim).subject(users.getEmail())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 20L * 60 * 60 * 1000)).and().signWith(getKey())
				.compact();
	}

	private Key getKey() {
		byte[] decode = Decoders.BASE64.decode(token);
		return Keys.hmacShaKeyFor(decode);
	}

	@Override
	public String extractUsername(String token) {
		Claims allClaims = extractClaims(token);
		return allClaims.getSubject();
	}

	private Claims extractClaims(String jwtToken) {
		try {
			return Jwts.parser().verifyWith(decryptKey()).build().parseSignedClaims(jwtToken).getPayload();
		} catch (SignatureException e) {
			throw new InvalidJwtToken("Invalid Token");
		} catch (ExpiredJwtException e) {
			throw new JwtTokenExpaired("Token Expaired");
		} catch (Exception e) {
			throw e;
		}

	}

	private SecretKey decryptKey() {
		byte[] decode = Decoders.BASE64.decode(token);
		return Keys.hmacShaKeyFor(decode);
	}

	@Override
	public boolean validateToken(String token, UserDetails loadUserByUsername) {
		Claims extractClaims = extractClaims(token);
		boolean isExpaired = extractClaims.getExpiration().before(new Date(System.currentTimeMillis()));
		if (extractClaims.getSubject().equalsIgnoreCase(loadUserByUsername.getUsername()) && !isExpaired) {
			return true;
		}
		return false;
	}
}
