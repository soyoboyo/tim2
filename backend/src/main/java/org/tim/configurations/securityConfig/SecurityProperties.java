package org.tim.configurations.securityConfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "security.jwt")
public class SecurityProperties {

	private String clientId;
	private String clientSecret;
	private String tokenEndpoint;
	private Resource key;

}
