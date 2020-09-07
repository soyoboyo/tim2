package com.oauth.server;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityTestsIT {

	@Autowired
	private WebApplicationContext webapp;


	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webapp)
				.apply(springSecurity())
				.build();
	}

	@Test
	public void whenUserLoginWithCorrectCredentialsThen() throws Exception {
		mockMvc.perform(post("/oauth/token")
				.param("username", "prog")
				.param("password", "prog")
				.param("grant_type", "password")
				.header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("timApp:OcadoProject".getBytes())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.access_token", any(String.class)))
				.andExpect(jsonPath("$.token_type", is("bearer")))
				.andExpect(jsonPath("$.refresh_token", any(String.class)))
				.andExpect(jsonPath("$.expires_in", is(3599)))
				.andExpect(jsonPath("$.scope", is("read write")))
				.andDo(print());
	}

	@Test
	public void whenUserLoginWithWrongCredentials() throws Exception {
		mockMvc.perform(post("/oauth/token")
				.param("username", "bad")
				.param("password", "credentials")
				.param("grant_type", "password")
				.header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("timApp:OcadoProject".getBytes())))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error", is("invalid_grant")))
				.andExpect(jsonPath("$.error_description", is("Bad credentials")))
				.andDo(print());
	}
}
