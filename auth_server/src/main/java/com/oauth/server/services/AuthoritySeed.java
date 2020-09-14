package com.oauth.server.services;

import com.oauth.server.entities.OauthClientDetails;
import com.oauth.server.entities.Principal;
import com.oauth.server.entities.User;
import com.oauth.server.repositories.OauthClientDetailsRepository;
import com.oauth.server.repositories.PrincipalRepository;
import com.oauth.server.repositories.UserRepository;
import com.oauth.server.utils.DefaultUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static com.oauth.server.utils.ConstantParameters.*;

@Service
@RequiredArgsConstructor
public class AuthoritySeed {

	private final OauthClientDetailsRepository oauthClientDetailsRepository;
	private final PrincipalRepository principalRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder bCryptPasswordEncoder;

	@PostConstruct
	public void addUsers() {
		for (DefaultUsers defaultUser : DefaultUsers.values()) {
			Principal principal = principalRepository.save(new Principal(defaultUser.roleType));
			userRepository.save(new User(defaultUser.defaultCred, bCryptPasswordEncoder.encode(defaultUser.defaultCred), principal));
		}

	}

	@PostConstruct
	public void addOAuthClients() {
		oauthClientDetailsRepository.save(new OauthClientDetails("timApp",
				bCryptPasswordEncoder.encode("OcadoProject"), CLIENT_SCOPE, CLIENT_GRANT_TYPES, "ROLE_CLIENT", SESSION_EXPIRE_SECONDS));
	}
}
