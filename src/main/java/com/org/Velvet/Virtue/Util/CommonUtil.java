package com.org.Velvet.Virtue.Util;

import jakarta.servlet.http.HttpServletRequest;

public class CommonUtil {

	public static String getUrl(HttpServletRequest req) {

		String url = req.getRequestURL().toString();
		String requestURI = req.getRequestURI();
		return url.replace(requestURI, "");
	}
}
