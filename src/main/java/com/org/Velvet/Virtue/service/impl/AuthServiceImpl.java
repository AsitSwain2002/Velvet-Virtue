package com.org.Velvet.Virtue.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.org.Velvet.Virtue.Dto.RequestDto;
import com.org.Velvet.Virtue.Dto.ResponseDto;
import com.org.Velvet.Virtue.Dto.UsersDto;
import com.org.Velvet.Virtue.Model.Users;
import com.org.Velvet.Virtue.Repo.UsersRepo;
import com.org.Velvet.Virtue.service.AuthService;
import com.org.Velvet.Virtue.service.JwtService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UsersRepo usersRepo;
	@Autowired
	private ModelMapper mapper;

	@Override
	public ResponseDto login(RequestDto requestDto) {
		ResponseDto responseDto = new ResponseDto();
		Users user = usersRepo.findByEmail(requestDto.getUserName());
		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(requestDto.getUserName(), requestDto.getPassword()));
		if (authenticate.isAuthenticated()) {
			responseDto.setUser(mapper.map(user, UsersDto.class));
			responseDto.setToken(jwtService.generateTooken(user));
			return responseDto;
		}
		return null;
	}

}
