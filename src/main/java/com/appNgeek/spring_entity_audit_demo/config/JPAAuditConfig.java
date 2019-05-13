package com.appNgeek.spring_entity_audit_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.appNgeek.spring_entity_audit_demo.domain.User;

@Configuration
@EnableJpaAuditing
public class JPAAuditConfig {

	@Bean
	public AuditorAware<User> auditorProvider() {
		return new UserAuditorAware();
	}

}