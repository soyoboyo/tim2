package com.oauth.server.services;

import com.oauth.server.entities.User;
import com.oauth.server.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import java.util.Arrays;

@Repository
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user == null)
            throw new UsernameNotFoundException("Unauthorized");
        return new org.springframework.security.core.userdetails.User(
                email,
                user.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority(user.getPrincipal().getRole())));
    }
}
