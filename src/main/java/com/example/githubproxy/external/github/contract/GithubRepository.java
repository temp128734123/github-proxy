package com.example.githubproxy.external.github.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class GithubRepository {
    private String name;
    private String ownerUsername;
    private boolean fork;

    public String getName() {
        return name;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public boolean isFork() {
        return fork;
    }

    @JsonProperty("owner")
    private void extractUsername(Map<String, Object> owner) {
        this.ownerUsername = (String) owner.get("login");
    }
}