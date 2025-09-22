package com.org.Velvet.Virtue.SecurityCofig;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class AuditConfig implements AuditorAware<Integer> {

	@Override
	public Optional<Integer> getCurrentAuditor() {

		return Optional.of(1);
	}

}
