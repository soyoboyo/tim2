package com.oauth.server.configuration.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Getter
@Configuration
public class SecurityProperties {

    @Value("${security.jwt.key-store}")
    private Resource keyStore;

    @Value("${security.jwt.key-store-password}")
    private String keyStorePassword;

    @Value("${security.jwt.key-pair-alias}")
    private String keyPairAlias;

    @Value("${security.jwt.key-pair-password}")
    private String keyPairPassword;
}
