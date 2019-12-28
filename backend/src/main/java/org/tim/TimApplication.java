package org.tim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class})
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TimApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimApplication.class, args);
	}
}
