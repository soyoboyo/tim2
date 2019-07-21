package org.tim.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tim.DTOs.UserPrincipal;
import org.tim.services.UserService;
import org.tim.utils.Mapping;

import java.security.Principal;


@RestController
@RequestMapping(Mapping.USER)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(Mapping.GET)
    public UserPrincipal getPrincipal(Principal principal) {
        return userService.getUserPrincipal(principal);
    }
}
