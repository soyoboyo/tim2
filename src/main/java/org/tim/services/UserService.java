package org.tim.services;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.tim.DTOs.UserPrincipal;

@Service
public class UserService {

    public UserPrincipal getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            String currentUserRole = ((GrantedAuthority)authentication.getAuthorities().toArray()[0]).getAuthority();
            return new UserPrincipal(currentUserName, currentUserRole);
        }
        return null;
    }
}
