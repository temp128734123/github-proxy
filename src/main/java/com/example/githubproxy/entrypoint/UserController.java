package com.example.githubproxy.entrypoint;

import com.example.githubproxy.logic.RepositoryService;
import com.example.githubproxy.contract.RepositoriesDto;
import com.example.githubproxy.model.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final RepositoryService repositoryService;
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @GetMapping(value = "/{username}/repositories")
    public RepositoriesDto getRepositories(@PathVariable String username) {
        log.info("Requesting repositories of: {}", username);
        List<Repository> repositories = repositoryService.getAllNonForked(username);
        return RepositoriesDto.from(repositories);
    }
}
