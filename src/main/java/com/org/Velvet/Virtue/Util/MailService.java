package com.org.Velvet.Virtue.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.org.Velvet.Virtue.Dto.MailData;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class MailService {

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String sender;

	public void send(MailData mailData) throws MessagingException {

		MimeMessage createMimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(createMimeMessage);
		helper.setSubject(mailData.getSubject());
		helper.setTo(mailData.getTo());
		helper.setText(mailData.getMessage(), true);
		helper.setFrom(sender);
		mailSender.send(createMimeMessage);
	}
} 
