package com.org.Velvet.Virtue.SecurityCofig;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openApi() {

		OpenAPI openAPI = new OpenAPI();
		Info info = new Info();
		info.setTitle("Velvet-Virtue");
		info.setDescription("Shopping Aplication");
		info.setVersion("1.0.0");

		SecurityScheme scheme = new SecurityScheme();
		scheme.name("Authorization").scheme("bearer").type(Type.HTTP).bearerFormat("JWT").in(In.HEADER);

		Components comp = new Components().addSecuritySchemes("Token", scheme);

		openAPI.setInfo(info);
		openAPI.setComponents(comp);
		openAPI.setSecurity(List.of(new SecurityRequirement().addList("Token")));
		return openAPI;
	}
}
