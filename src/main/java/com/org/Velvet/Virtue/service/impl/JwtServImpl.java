package com.org.Velvet.Virtue.service.impl;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

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

		return Jwts.builder().claims().add(claim).subject(users.getEmail())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 20L * 60 * 60 * 1000)).and().signWith(getKey())
				.compact();
	}

	private Key getKey() {
		byte[] decode = Decoders.BASE64.decode(token);
		return Keys.hmacShaKeyFor(decode);
	}

}
