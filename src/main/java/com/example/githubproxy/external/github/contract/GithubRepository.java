package com.example.githubproxy.external.github.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class GithubRepository {
    private String name;
    private String ownerUsername;
    private boolean fork;

    @JsonProperty("owner")
    private void extractUsername(Map<String, Object> owner) {
        this.ownerUsername = (String) owner.get("login");
    }
}