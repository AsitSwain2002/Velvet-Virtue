package com.org.Velvet.Virtue.SecurityCofig;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import com.org.Velvet.Virtue.Util.CommonUtil;

public class AuditConfig implements AuditorAware<Integer> {

	@Override
	public Optional<Integer> getCurrentAuditor() {

		return Optional.of(CommonUtil.getLoggedUser().getId());
	}

}
