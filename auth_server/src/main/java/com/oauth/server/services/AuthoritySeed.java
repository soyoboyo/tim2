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
		Principal roleDeveloper = principalRepository.save(new Principal("ROLE_DEVELOPER"));
		Principal roleTranslator = principalRepository.save(new Principal("ROLE_TRANSLATOR"));
		Principal roleAdminTranslator = principalRepository.save(new Principal("ROLE_ADMIN_TRANSLATOR"));
		Principal roleAdminDeveloper = principalRepository.save(new Principal("ROLE_ADMIN_DEVELOPER"));
		Principal roleSuperAdmin = principalRepository.save(new Principal("ROLE_SUPER_ADMIN"));

		userRepository.save(new User("prog", bCryptPasswordEncoder.encode("prog"), roleDeveloper));
		userRepository.save(new User("tran", bCryptPasswordEncoder.encode("tran"), roleTranslator));
		userRepository.save(new User("adminProg", bCryptPasswordEncoder.encode("adminProg"), roleAdminDeveloper));
		userRepository.save(new User("adminTran", bCryptPasswordEncoder.encode("adminTran"), roleAdminTranslator));
		userRepository.save(new User("admin", bCryptPasswordEncoder.encode("admin"), roleSuperAdmin));
	}

	@PostConstruct
	public void addOAuthClients() {
		oauthClientDetailsRepository.save(new OauthClientDetails("timApp",
				bCryptPasswordEncoder.encode("OcadoProject"), "read,write",
				"password,refresh_token,client_credentials",
				"ROLE_CLIENT", 60 * 60));
	}
}
