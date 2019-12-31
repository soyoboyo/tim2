package org.tim.controllers;

import io.swagger.annotations.ApiOperation;
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

	@ApiOperation(
			value = "Get logged user details",
			notes = "Returns information about user with parameters:\n" +
					"* username -> unique user name used to authenticate\n" +
					"* role -> user role (ROLE_DEVELOPER or ROLE_TRANSLATOR).\n" +
					"If user is not logged in then returns null.")
	@GetMapping(Mapping.GET)
	public UserPrincipal getPrincipal(Principal principal) {
		return userService.getUserPrincipal(principal);
	}

}
