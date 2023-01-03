package com.example.githubproxy.external.github.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Branch {
    private String name;
    private String lastCommitSha;

    @JsonProperty("commit")
    private void extractCommitSha(Map<String, Object> commit) {
        this.lastCommitSha = (String) commit.get("sha");
    }
}
