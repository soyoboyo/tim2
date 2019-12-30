package org.tim.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.tim.annotations.ToDo;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsConfig {

	@ToDo("Update Allowed Origins")
	@Bean
	public CorsFilter corsFilter() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOrigins(Collections.singletonList("*"));
		config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "Accept"));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE"));
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}
