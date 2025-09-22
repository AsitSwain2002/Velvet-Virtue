package com.org.Velvet.Virtue.SecurityCofig;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import jakarta.persistence.criteria.CriteriaBuilder.In;

@Configuration
public class AppConfig {

	@Bean
	public static ModelMapper mapper() {
		return new ModelMapper();
	}

	@Bean
	public AuditorAware<Integer> auditAware() {
		return new AuditConfig();
	}
}
