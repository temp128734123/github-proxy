package com.example.githubproxy.model;

import com.example.githubproxy.external.github.contract.Branch;

import java.util.List;

public record Repository(String repositoryName, String ownerUsername, List<Branch> branches) {
}
