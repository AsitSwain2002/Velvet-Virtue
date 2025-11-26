package com.org.Velvet.Virtue.Util;

import org.springframework.security.core.context.SecurityContextHolder;

import com.org.Velvet.Virtue.Model.Users;
import com.org.Velvet.Virtue.securityConfig.UserDetl;

import jakarta.servlet.http.HttpServletRequest;

public class CommonUtil {

	public static String getUrl(HttpServletRequest req) {

		String url = req.getRequestURL().toString();
		String requestURI = req.getRequestURI();
		return url.replace(requestURI, "");
	}

	public static Users getLoggedUser() {
		try {
			UserDetl udtl = (UserDetl) SecurityContextHolder.getContext().getAuthentication();
			return udtl.getUsers();
		} catch (Exception e) {
			throw e;
		}
	}
}
