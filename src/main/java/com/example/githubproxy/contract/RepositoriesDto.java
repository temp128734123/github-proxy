package com.example.githubproxy.contract;

import com.example.githubproxy.model.Repository;

import java.util.List;

public record RepositoriesDto(List<Repository> repositories) {
    public static RepositoriesDto from(List<Repository> repositories) {
        return new RepositoriesDto(repositories);
    }
}
