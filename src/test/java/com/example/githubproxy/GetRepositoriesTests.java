package com.example.githubproxy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GetRepositoriesTests {
    @Autowired
    private MockMvc mvc;

    @Test
    void shouldReturnNotFoundForNonexistentUser() throws Exception {
        mvc.perform(get("/users/kafjkadjkahwekjankwsakjnxkajcaca/repositories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(
                        """
                                {
                                	"status": 404,
                                	"message": "Username not found"
                                }
                                """
                ));
    }

    @Test
    void shouldReturnNotAcceptableForUnsupportedContentType() throws Exception {
        mvc.perform(get("/users/GeniusMarco/repositories")
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().json(
                        """
                                {
                                	"status": 406,
                                	"message": "Content type not supported"
                                }
                                """
                ));
    }

    @Test
    void shouldSkipForks() throws Exception {
        mvc.perform(get("/users/Temp4823712/repositories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("""
                                {
                                	"repositories": []
                                }
                                """));
    }

    @Test
    void shouldReturnRepositories() throws Exception {
        mvc.perform(get("/users/GeniusMarco/repositories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("""
                                {
                                	"repositories": [
                                		{
                                			"repositoryName": "cvanish-client",
                                			"ownerUsername": "GeniusMarco",
                                			"branches": [
                                				{
                                					"name": "master",
                                					"lastCommitSha": "5c8f9e76310141b12069cbfa8a0289006caa64f7"
                                				}
                                			]
                                		},
                                		{
                                			"repositoryName": "cvanish-server",
                                			"ownerUsername": "GeniusMarco",
                                			"branches": [
                                				{
                                					"name": "master",
                                					"lastCommitSha": "f8e2a445bff3c49a3de0d74fc0b39d1378772000"
                                				}
                                			]
                                		}
                                	]
                                }
                                """));
    }
}
