package io.swagger.api.steps.user;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java8.En;
import lombok.extern.slf4j.Slf4j;
import io.swagger.api.steps.BaseStepDefinitions;
import io.swagger.model.LoginDTO;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.lang.annotation.Annotation;

@Slf4j
class LoginStepDefinitions extends BaseStepDefinitions implements En {

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private ResponseEntity<String> response;
    private LoginDTO dto;

    public LoginStepDefinitions() {
        When("^I call the login endpoint$", () -> {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Type", "application/json");

            HttpEntity<String> request = new HttpEntity<String>(mapper.writeValueAsString(
                    dto),
                    httpHeaders);
            response = restTemplate.postForEntity(getBaseUrl() + "/api/login",
                    request, String.class);
        });

        Then("^I receive a status of (\\d+)$", (Integer status) -> {
            Assertions.assertEquals(status, response.getStatusCodeValue());
        });

        And("^I get a JWT-token$", () -> {
            JSONObject jsonObject = new JSONObject(response.getBody());
            String token = jsonObject.getString("JWTtoken");
            Assertions.assertTrue(token.startsWith("ey"));
        });
        Given("^I have a valid user object$", () -> {
            dto = new LoginDTO("musavir@inholland.nl", "test12345");
        });
        Given("^I have an invalid user object$", () -> {
            dto = new LoginDTO("", "");
        });
    }

}

