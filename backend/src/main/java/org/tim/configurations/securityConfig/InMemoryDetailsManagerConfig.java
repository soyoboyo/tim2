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

    @Value("${usersConfig}")
    private String pathToUsersConfig;

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() throws IOException {
        final Properties users = new Properties();
        File file;
        if(pathToUsersConfig.isEmpty()) {
            file = new File(new ClassPathResource("/principals.json").getURI());
        } else {
            file = new File(pathToUsersConfig);
        }
        byte[] data;
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            data = new byte[(int) file.length()];
            fileInputStream.read(data);
        }
        JSONObject jsonObject = new JSONObject(new String(data, "UTF-8"));
        JSONArray principalsArray = jsonObject.getJSONArray("users");
        JSONObject principals;
        for ( int i = 0; i < principalsArray.length(); i++) {
            principals = (JSONObject) principalsArray.get(i);
            users.put(principals.getString("username"),
                    bCryptPasswordEncoder.encode(principals.getString("password")) +
                            ",ROLE_" + principals.getString("role") + ",enabled");
        }
        return new InMemoryUserDetailsManager(users);
    }
}
