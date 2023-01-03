package com.example.githubproxy;

import com.example.githubproxy.contract.ApplicationExceptionDto;
import com.example.githubproxy.contract.RepositoriesDto;
import com.example.githubproxy.external.github.contract.Branch;
import com.example.githubproxy.model.Repository;
import com.github.tomakehurst.wiremock.http.Body;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 9081)
public class GetRepositoriesWiremockTests {
    private static final String USER_1 = "user1";
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate rest;

    @Test
    void shouldReturnNotFoundForNonexistentUser() {
        stubFor(get("/users/" + USER_1 + "/repos")
                .willReturn(status(404).withResponseBody(new Body(
                        """
                                {
                                	"message": "Not Found",
                                	"documentation_url": "https://docs.github.com/rest/reference/repos#list-repositories-for-a-user"
                                }
                                """
                ))));

        ResponseEntity<ApplicationExceptionDto> response = getRepositories(USER_1, ApplicationExceptionDto.class);

        assertEquals(404, response.getStatusCode().value());
        assertEquals(new ApplicationExceptionDto(404, "Username not found"), response.getBody());
    }

    @Test
    void shouldReturnNotAcceptableForUnsupportedContentType() {
        ResponseEntity<ApplicationExceptionDto> response = getRepositories(USER_1, MediaType.APPLICATION_XML, ApplicationExceptionDto.class);

        assertEquals(406, response.getStatusCode().value());
        assertEquals(new ApplicationExceptionDto(406, "Content type not supported"), response.getBody());
    }

    @Test
    void shouldSkipForks() {
        stubFor(get("/users/" + USER_1 + "/repos")
                .willReturn(status(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withResponseBody(new Body(
                                """
                                        [
                                            {
                                                "name": "fork1",
                                                "owner": {
                                                    "login": "user1"
                                                },
                                                "fork": true
                                            }
                                        ]
                                        """
                        ))));

        ResponseEntity<RepositoriesDto> response = getRepositories(USER_1, RepositoriesDto.class);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(new RepositoriesDto(Collections.emptyList()), response.getBody());
    }

    @Test
    void shouldReturnNonForkedRepositories() {
        stubFor(get("/users/" + USER_1 + "/repos")
                .willReturn(status(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withResponseBody(new Body(
                                """
                                        [
                                            {
                                                "name": "repo1",
                                                "owner": {
                                                    "login": "user1"
                                                },
                                                "fork": false
                                            },
                                            {
                                                "name": "repo2",
                                                "owner": {
                                                    "login": "user1"
                                                },
                                                "fork": false
                                            }
                                        ]
                                        """
                        ))));

        stubFor(get("/repos/" + USER_1 + "/repo1/branches")
                .willReturn(status(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withResponseBody(new Body(
                                """
                                        [
                                        	{
                                        		"name": "master",
                                        		"commit": {
                                        			"sha": "f8e2a445bff3c49a3de0d74fc0b39d1378772000",
                                        			"url": "https://api.github.com/repos/user1/repo1/commits/f8e2a445bff3c49a3de0d74fc0b39d1378772000"
                                        		},
                                        		"protected": false
                                        	}
                                        ]
                                        """
                        ))));

        stubFor(get("/repos/" + USER_1 + "/repo2/branches")
                .willReturn(status(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withResponseBody(new Body(
                                """
                                        [
                                        	{
                                        		"name": "master",
                                        		"commit": {
                                        			"sha": "4347d0f8ba661234a8eadc005e2e1d1b646c9682",
                                        			"url": "https://api.github.com/repos/user1/repo2/commits/f8e2a445bff3c49a3de0d74fc0b39d1378772000"
                                        		},
                                        		"protected": false
                                        	}
                                        ]
                                        """
                        ))));

        ResponseEntity<RepositoriesDto> response = getRepositories(USER_1, RepositoriesDto.class);

        RepositoriesDto expectedRepositories = new RepositoriesDto(List.of(
                new Repository("repo1", USER_1,
                        List.of(new Branch("master", "f8e2a445bff3c49a3de0d74fc0b39d1378772000"))),
                new Repository("repo2", USER_1,
                        List.of(new Branch("master", "4347d0f8ba661234a8eadc005e2e1d1b646c9682")))

        ));
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedRepositories, response.getBody());
    }

    private <T> ResponseEntity<T> getRepositories(String user, Class<T> responseType) {
        return rest.getForEntity(getRepositoriesForUserUrl(user), responseType);
    }

    private <T> ResponseEntity<T> getRepositories(String user, MediaType mediaType, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return rest.exchange(new RequestEntity<>(headers, HttpMethod.GET, URI.create(getRepositoriesForUserUrl(user))), responseType);
    }

    private String getRepositoriesForUserUrl(String user) {
        return String.format("http://localhost:%d/users/%s/repositories", port, user);
    }
}
