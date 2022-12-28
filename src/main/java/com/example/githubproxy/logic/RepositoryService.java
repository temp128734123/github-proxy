package com.example.githubproxy.logic;

import com.example.githubproxy.external.github.client.GithubClient;
import com.example.githubproxy.external.github.contract.GithubRepository;
import com.example.githubproxy.model.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepositoryService {
    private final GithubClient githubClient;

    public RepositoryService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public List<Repository> getAllNonForked(String username) {
        List<GithubRepository> repositories = githubClient.getRepositories(username);
        return repositories
                .parallelStream()
                .filter(repo -> !repo.isFork())
                .map(repo -> new Repository(repo.getName(), repo.getOwnerUsername(), githubClient.getBranches(username, repo.getName())))
                .collect(Collectors.toList());
    }
}
