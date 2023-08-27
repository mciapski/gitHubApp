package com.GitHub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GitHubBranchesDto {
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "commit")
    private Commit commit;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Commit {
        @JsonProperty(value = "sha")
        private String sha;
    }
}
