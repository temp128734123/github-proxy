package com.example.githubproxy.external.github.client;

import com.example.githubproxy.external.github.contract.Branch;
import com.example.githubproxy.external.github.contract.GithubRepository;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "github-api", url = "https://api.github.com")
public interface GithubClient {

    @RequestMapping(method = RequestMethod.GET, value = "/users/{username}/repos")
    List<GithubRepository> getRepositories(@PathVariable String username);

    @RequestMapping(method = RequestMethod.GET, value = "/repos/{username}/{repo}/branches")
    List<Branch> getBranches(@PathVariable String username, @PathVariable String repo);

}
