package com.org.Velvet.Virtue.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailData {

	private String to;
	private String subject;
	private String title;
	private String message;
}
