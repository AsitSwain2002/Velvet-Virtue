package com.org.Velvet.Virtue.securityConfig;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.org.Velvet.Virtue.Model.Users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetl implements UserDetails {

	@Autowired
	private Users users;

	public UserDetl(Users users) {
		super();
		this.users = users;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		ArrayList<GrantedAuthority> authority = new ArrayList<GrantedAuthority>();
		users.getRoles().stream().forEach(e -> {
			authority.add(new SimpleGrantedAuthority("ROLE_" + e.getName()));
		});
		return authority;
	}

	@Override
	public String getPassword() {

		return users.getPassword();
	}

	@Override
	public String getUsername() {

		return users.getEmail();
	}

}
