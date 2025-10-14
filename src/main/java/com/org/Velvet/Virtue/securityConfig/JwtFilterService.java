package com.org.Velvet.Virtue.securityConfig;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.Velvet.Virtue.Util.GenericResponse;
import com.org.Velvet.Virtue.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilterService extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserDetlServImpl detlServImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String header = request.getHeader("Authorization");

			String userName = null;
			String token = null;
			if (header != null && header.startsWith("Bearer ")) {
				token = header.substring(7);
				userName = jwtService.extractUsername(token);

			}
			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails loadUserByUsername = detlServImpl.loadUserByUsername(userName);
				boolean validateToken = jwtService.validateToken(token, loadUserByUsername);
				if (validateToken) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							loadUserByUsername, null, loadUserByUsername.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

				}
			}
		} catch (Exception e) {
			generateExpenceResponse(e, response);
			return;
		}
		filterChain.doFilter(request, response);

	}

	private void generateExpenceResponse(Exception e, HttpServletResponse response)
			throws JsonProcessingException, IOException {
		response.setContentType("application/json");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		Object errorMessage = GenericResponse.builder().msg(e.getMessage()).status(HttpStatus.UNAUTHORIZED.value())
				.build().createWithOutData().getBody();
		response.getWriter().write(new ObjectMapper().writeValueAsString(errorMessage));

	}

}
