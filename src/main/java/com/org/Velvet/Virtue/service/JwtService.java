package com.org.Velvet.Virtue.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.org.Velvet.Virtue.Model.Users;

public interface JwtService {

	String generateTooken(Users users);

	String extractUsername(String token);

	boolean validateToken(String token, UserDetails loadUserByUsername);

}
