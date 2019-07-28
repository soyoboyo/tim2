package org.tim.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.tim.configuration.SpringTestsCustomExtension;

import static org.tim.constants.Mappings.*;
import static org.junit.jupiter.api.Assertions.*;

public class BasicSecurityTestIT extends SpringTestsCustomExtension {

    @Autowired
    private TestRestTemplate template;

    private final String BASE_URL = "http://localhost:8081";

    @Test
    public void whenGivenAuthRequestThenGetAllMessagesShouldSucceedWithStatusOk() {
        ResponseEntity<String> result = template.withBasicAuth("prog", "prog")
                .getForEntity( BASE_URL + API_VERSION + LOCALES + LANGUAGES + GET_ALL, String.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void whenGivenWrongAuthRequestThenGetAllMessagesShouldFailedWithStatusUnauthorized() {
        ResponseEntity<String> result = template.withBasicAuth("badUsername", "passwordWithMistake")
                .getForEntity(BASE_URL + API_VERSION + MESSAGE + GET_ALL, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }

    //TODO in next branch i will try to create new tests (there is no need to run spring context)
}
