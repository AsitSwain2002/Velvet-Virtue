package com.org.Velvet.Virtue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAware")
public class VelvetVirtueApplication {

	public static void main(String[] args) {
		SpringApplication.run(VelvetVirtueApplication.class, args);
	}

}
