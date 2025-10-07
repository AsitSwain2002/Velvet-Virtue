package com.org.Velvet.Virtue.Util;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {

	public static ResponseEntity<?> withOutData(String msg, HttpStatusCode statusCode) {
		GenericResponse gr = GenericResponse.builder().msg(msg).status(statusCode.value()).build();
		return gr.createWithOutData();
	}

	public static ResponseEntity<?> withData(String msg, Object data, HttpStatusCode statusCode) {
		GenericResponse gr = GenericResponse.builder().msg(msg).data(data).status(statusCode.value()).build();
		return gr.createWithData();
	}

	public static ResponseEntity<?> exceptionDetails(String msg, Object data, HttpStatusCode statusCode) {
		GenericResponse gr = GenericResponse.builder().msg(msg).data(data).status(statusCode.value()).build();
		return gr.exceptionDetails();
	}
}
