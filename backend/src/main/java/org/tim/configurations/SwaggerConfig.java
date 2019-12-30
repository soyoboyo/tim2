package org.tim.configurations;

import com.fasterxml.classmate.TypeResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.tim.configurations.securityConfig.SecurityProperties;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class SwaggerConfig {

	private final TypeResolver typeResolver;
	private final SecurityProperties securityProperties;

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build()
				.securitySchemes(Collections.singletonList(securitySchema()))
				.securityContexts(Collections.singletonList(securityContext()))
				.pathMapping("/")
				.useDefaultResponseMessages(false).apiInfo(apiInfo())
				.alternateTypeRules(
						AlternateTypeRules.newRule(
								typeResolver.resolve(Map.class, String.class,
										typeResolver.resolve(Map.class, String.class, typeResolver.resolve(List.class, String.class))),
								typeResolver.resolve(Map.class, String.class, WildcardType.class), Ordered.HIGHEST_PRECEDENCE));
	}

	private SecurityScheme securitySchema() {
		SecurityScheme oauth = new OAuthBuilder().name("spring_oauth")
				.grantTypes(grantTypes())
				.scopes(Arrays.asList(scopes()))
				.build();
		return oauth;
	}

	private AuthorizationScope[] scopes() {
		AuthorizationScope[] scopes = {
				new AuthorizationScope("read", "for GET"),
				new AuthorizationScope("write", "for POST, DELETE")};
		return scopes;
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(
						Arrays.asList(new SecurityReference("spring_oauth", scopes())))
				.forPaths(PathSelectors.regex("/.*"))
				.build();
	}

	@Bean
	public SecurityConfiguration security() {
		return SecurityConfigurationBuilder.builder()
				.clientId(securityProperties.getClientId())
				.clientSecret(securityProperties.getClientSecret())
				.scopeSeparator(" ")
				.build();
	}


	public List<GrantType> grantTypes() {
		List<GrantType> grantTypes = new ArrayList<>();
		grantTypes.add(new ResourceOwnerPasswordCredentialsGrant(securityProperties.getTokenEndpoint()));
		return grantTypes;
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("TIM-Project").version("2.0.0").build();
	}
}
