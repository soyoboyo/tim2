package com.oauth.server.services;

import com.oauth.server.entities.OauthClientDetails;
import com.oauth.server.entities.Principal;
import com.oauth.server.entities.User;
import com.oauth.server.repositories.OauthClientDetailsRepository;
import com.oauth.server.repositories.PrincipalRepository;
import com.oauth.server.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class AuthoritySeed {

    private final OauthClientDetailsRepository oauthClientDetailsRepository;
    private final PrincipalRepository principalRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    public void addUsers() {
        Principal roleUser = principalRepository.save(new Principal("ROLE_DEVELOPER"));
        Principal roleGuest = principalRepository.save(new Principal("ROLE_TRANSLATOR"));

        userRepository.save(new User("prog", bCryptPasswordEncoder.encode("prog"), roleUser));
        userRepository.save(new User("tran", bCryptPasswordEncoder.encode("tran"), roleGuest));
    }

    @PostConstruct
    public void addOAuthClients() {
        oauthClientDetailsRepository.save(new OauthClientDetails("timApp",
                bCryptPasswordEncoder.encode("OcadoProject"), "read,write",
                "password,refresh_token,client_credentials",
                "ROLE_CLIENT", 8 * 60));
    }
}
