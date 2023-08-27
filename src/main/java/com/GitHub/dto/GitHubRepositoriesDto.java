package com.GitHub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GitHubRepositoriesDto {
    @JsonProperty("name")
    private String repositoryName;
    @JsonProperty("owner")
    private Owner owner;
    @JsonProperty("fork")
    private boolean fork;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Owner {
        @JsonProperty("login")
        private String login;
    }

}
