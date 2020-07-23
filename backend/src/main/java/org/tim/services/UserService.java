package org.tim.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.tim.DTOs.UserPrincipal;

import java.security.Principal;

@Service
public class UserService {

	public UserPrincipal getUserPrincipal(Principal principal) {
		if (principal == null)
			return null;

		String currentUserName = principal.getName();
		String currentUserRole = ((GrantedAuthority) ((Authentication) principal).getAuthorities().toArray()[0]).getAuthority();
		return new UserPrincipal(currentUserName, currentUserRole);
	}
}
