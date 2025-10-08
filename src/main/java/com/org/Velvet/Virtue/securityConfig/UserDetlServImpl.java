package com.org.Velvet.Virtue.securityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.org.Velvet.Virtue.Model.Users;
import com.org.Velvet.Virtue.Repo.UsersRepo;

@Service
public class UserDetlServImpl implements UserDetailsService {

	@Autowired
	private UsersRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userRepo.findByEmail(username);
		return new UserDetl(user);
	}

}
