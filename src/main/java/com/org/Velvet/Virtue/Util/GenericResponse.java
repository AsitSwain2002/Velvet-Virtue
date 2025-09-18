package com.org.Velvet.Virtue.Util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import lombok.Builder;

@Builder
public class GenericResponse {

	private String msg;
	private Object data;
	private int status;

	public ResponseEntity<?> createWithData() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", msg);
		map.put("data", data);
		map.put("status", status);
		return new ResponseEntity(map, HttpStatusCode.valueOf(status));
	}

	public ResponseEntity<?> createWithOutData() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", msg);
		map.put("status", status);
		return new ResponseEntity(map, HttpStatusCode.valueOf(status));
	}
}
