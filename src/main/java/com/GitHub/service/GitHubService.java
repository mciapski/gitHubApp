package com.GitHub.service;

import com.GitHub.dto.GitHubBranchesDto;
import com.GitHub.dto.GitHubRepositoriesDto;
import com.GitHub.entity.GitHubEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GitHubService {


    private final RestTemplate restTemplate;

    public GitHubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<GitHubRepositoriesDto> getAllUserRepositories(String name) {
        if (Objects.equals(name, null)) {
            throw new IllegalArgumentException(" Input name is null");
        }
        URI uri = UriComponentsBuilder.fromHttpUrl("https://api.github.com/users/" + name + "/repos")
                .build().toUri();
        ResponseEntity<GitHubRepositoriesDto[]> responseEntity =
                restTemplate.getForEntity(uri, GitHubRepositoriesDto[].class);
        GitHubRepositoriesDto[] repoArray = responseEntity.getBody();
        return List.of(repoArray);
    }

    public List<GitHubRepositoriesDto> getAllNotForkedRepositories(List<GitHubRepositoriesDto> allRepos) {
        if (Objects.equals(allRepos, null)) {
            throw new IllegalArgumentException(" Input list of repositories is null");
        }
        if (allRepos.isEmpty()) {
            throw new IllegalArgumentException(" Input list of repositories is empty");
        }

        return allRepos.stream().filter(element -> !element.isFork()).collect(Collectors.toList());
    }

    public List<GitHubBranchesDto> getAllBranchesForRepository(GitHubRepositoriesDto repo) {
        String ownerName = repo.getOwner().getLogin();
        String repoName = repo.getRepositoryName();
        URI uri = UriComponentsBuilder.fromHttpUrl("https://api.github.com/repos/" + ownerName + "/" + repoName + "/branches")
                .build().toUri();
        ResponseEntity<GitHubBranchesDto[]> responseEntity =
                restTemplate.getForEntity(uri, GitHubBranchesDto[].class);
        GitHubBranchesDto[] branchArray = responseEntity.getBody();
        return List.of(branchArray);
    }

    public List<GitHubEntity> mapToEntityObject(List<GitHubRepositoriesDto> inputList) {
        if (inputList.isEmpty()) {
            throw new NullPointerException("Input list is empty");
        }
        List<GitHubEntity> resultList = new ArrayList<>();
        inputList.forEach(element -> {
            GitHubEntity gitHubEntity = new GitHubEntity();
            gitHubEntity.setRepositoryName(element.getRepositoryName());
            gitHubEntity.setOwnerLogin(element.getOwner().getLogin());
            gitHubEntity.setBranches(getAllBranchesForRepository(element));
            resultList.add(gitHubEntity);
        });
        return resultList;
    }

}

