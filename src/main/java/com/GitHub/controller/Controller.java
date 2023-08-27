package com.GitHub.controller;

import com.GitHub.entity.GitHubEntity;
import com.GitHub.exceptions.BadHeaderException;
import com.GitHub.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
Acceptance criteria:



As an api consumer, given username and header “Accept: application/json”, I would like to list all his github repositories, which are not forks. Information, which I require in the response, is:

Repository Name

Owner Login

For each branch it’s name and last commit sha

As an api consumer, given not existing github user, I would like to receive 404 response in such a format:

{



    “status”: ${responseCode}



    “Message”: ${whyHasItHappened}



}



As an api consumer, given header “Accept: application/xml”, I would like to receive 406 response in such a format:

    {



    “status”: ${responseCode}



    “Message”: ${whyHasItHappened}



}



Notes:



Please full-fill the given acceptance criteria, delivering us your best code compliant with industry standards.

Please use https://developer.github.com/v3 as a backing API

Application should have a proper README.md file
 */

@RestController
@CrossOrigin(maxAge = 3600)
public class Controller {
    @Autowired
    private GitHubService gitHubService;

    @RequestMapping(value = "/github-repository", method = RequestMethod.GET)
    public List<GitHubEntity> getGitHubEntityList(@RequestHeader("Content-Type") String contentType, @RequestParam(value = "name") String name) {
        if (!contentType.equals("application/json")) {
            throw new BadHeaderException();
        }
            return gitHubService.mapToEntityObject(gitHubService.getAllNotForkedRepositories(gitHubService.getAllUserRepositories(name)));
    }
}
