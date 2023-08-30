package com.GitHub.controller;

import com.GitHub.entity.GitHubEntity;
import com.GitHub.exceptions.BadHeaderException;
import com.GitHub.service.GitHubService;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@CrossOrigin(maxAge = 3600)
public class Controller {
    private final GitHubService gitHubService;

    public Controller(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @RequestMapping(value = "/github-repository", method = RequestMethod.GET)
    public List<GitHubEntity> getGitHubEntityList(@RequestHeader("Content-Type") String contentType,
                                                  @RequestParam(value = "name") String name) {
        if (!contentType.equals("application/json")) {
            throw new BadHeaderException();
        }
        return gitHubService.mapToEntityObject(gitHubService.getAllNotForkedRepositories(gitHubService.getAllUserRepositories(name)));
    }
}
