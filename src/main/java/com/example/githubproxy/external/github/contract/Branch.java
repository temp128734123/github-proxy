package com.example.githubproxy.external.github.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Branch {
    private String name;
    private String lastCommitSha;

    public String getName() {
        return name;
    }

    public String getLastCommitSha() {
        return lastCommitSha;
    }

    @JsonProperty("commit")
    private void extractCommitSha(Map<String, Object> commit) {
        this.lastCommitSha = (String) commit.get("sha");
    }
}
