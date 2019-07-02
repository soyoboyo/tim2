package org.tim.configurations.securityConfig;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.io.*;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class InMemoryDetailsManagerConfig {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() throws IOException {
        final Properties users = new Properties();
        users.put("prog", bCryptPasswordEncoder.encode("prog") + ",ROLE_DEVELOPER" + ",enabled");
        users.put("tran", bCryptPasswordEncoder.encode("tran") + ",ROLE_TRANSLATOR" + ",enabled");
        users.put("ci", bCryptPasswordEncoder.encode("ci") + ",ROLE_CI/CD" + ",enabled");

        return new InMemoryUserDetailsManager(users);
    }
}
