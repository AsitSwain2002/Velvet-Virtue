package com.org.Velvet.Virtue.SecurityCofig;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public static ModelMapper mapper() {
		return new ModelMapper();
	}
}
