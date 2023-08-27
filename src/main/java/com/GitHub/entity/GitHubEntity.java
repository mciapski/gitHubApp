package com.GitHub.entity;

import com.GitHub.dto.GitHubBranchesDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
Repository Name

Owner Login

For each branch it’s name and last commit sha
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GitHubEntity {
    @JsonProperty(value = "Repository Name")
    private String repositoryName;
    @JsonProperty(value = "Owner Login")
    private String ownerLogin;
    @JsonProperty(value = "Branches")
    private List<GitHubBranchesDto> branches;

}
