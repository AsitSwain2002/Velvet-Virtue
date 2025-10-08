package com.org.Velvet.Virtue.service;

import com.org.Velvet.Virtue.Model.Users;

public interface JwtService {

	String generateTooken(Users users);
}
