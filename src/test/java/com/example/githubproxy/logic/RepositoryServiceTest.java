package com.example.githubproxy.logic;

import com.example.githubproxy.external.github.client.GithubClient;
import com.example.githubproxy.external.github.contract.Branch;
import com.example.githubproxy.external.github.contract.GithubRepository;
import com.example.githubproxy.model.Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepositoryServiceTest {
    private static final String USER_1 = "user1";
    private static final String REPO_1 = "repo1";
    private static final String REPO_2 = "repo2";
    @Mock
    private static GithubClient githubClient;
    @InjectMocks
    private RepositoryService repositoryService;

    @Test
    void shouldReturnAllRepositoriesWhenOnlyNonForksPresent() {
        when(githubClient.getRepositories(USER_1))
                .thenReturn(List.of(
                        new GithubRepository(REPO_1, USER_1, false),
                        new GithubRepository(REPO_2, USER_1, false)
                ));
        List<Branch> firstRepoBranches = List.of(new Branch(REPO_1, "aaa"));
        when(githubClient.getBranches(USER_1, REPO_1))
                .thenReturn(firstRepoBranches);
        List<Branch> secondRepoBranches = List.of(new Branch(REPO_2, "xxx"));
        when(githubClient.getBranches(USER_1, REPO_2))
                .thenReturn(secondRepoBranches);

        List<Repository> actualRepositories = repositoryService.getAllNonForked(USER_1);

        List<Repository> expectedRepositories = List.of(
                new Repository(REPO_1, USER_1, firstRepoBranches),
                new Repository(REPO_2, USER_1, secondRepoBranches));
        assertEquals(expectedRepositories, actualRepositories);
    }

    @Test
    void shouldReturnNonForkedOnly() {
        when(githubClient.getRepositories(USER_1))
                .thenReturn(List.of(
                        new GithubRepository(REPO_1, USER_1, true),
                        new GithubRepository(REPO_2, USER_1, false)
                ));
        List<Branch> branches = List.of(new Branch(REPO_2, "xxx"));
        when(githubClient.getBranches(USER_1, REPO_2))
                .thenReturn(branches);

        List<Repository> repositories = repositoryService.getAllNonForked(USER_1);

        assertEquals(List.of(new Repository(REPO_2, USER_1, branches)), repositories);
    }

    @Test
    void shouldReturnEmptyListWhenOnlyForksPresent() {
        when(githubClient.getRepositories(USER_1))
                .thenReturn(List.of(
                        new GithubRepository("fork1", USER_1, true),
                        new GithubRepository("fork2", USER_1, true)
                ));

        List<Repository> repositories = repositoryService.getAllNonForked(USER_1);

        assertEquals(emptyList(), repositories);
    }
    
    @Test
    void shouldReturnEmptyListWhenNoRepositoriesPresent() {
        when(githubClient.getRepositories(USER_1))
                .thenReturn(emptyList());

        List<Repository> repositories = repositoryService.getAllNonForked(USER_1);

        assertEquals(emptyList(), repositories);
    }
}
