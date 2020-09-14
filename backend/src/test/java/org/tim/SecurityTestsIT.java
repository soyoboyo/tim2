package org.tim;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.tim.configuration.SpringTestsCustomExtension;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.tim.constants.Mappings.*;

public class SecurityTestsIT extends SpringTestsCustomExtension {

	@Autowired
	private WebApplicationContext webapp;

	private MockMvc mockMvc;


	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webapp)
				.apply(springSecurity())
				.build();
	}

	@Test
	@DisplayName("When unauthorized user trying to access resources that require authorization then return unauthorized response")
	void whenUnauthorizedUserTryToGetSecuredResourcesThenReturnUnauthorizedResponse() throws Exception {
		mockMvc.perform(get(BASE_URL + API_VERSION + TRANSLATION + VERSION, 1L))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.error", is("unauthorized")))
				.andExpect(jsonPath("$.error_description", is("Full authentication is required to access this resource")))
				.andDo(print());
	}
}

