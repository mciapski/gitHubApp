package com.GitHub.service;

import com.GitHub.dto.GitHubRepositoriesDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class GitHubServiceTest {

    GitHubService gitHubService;

    RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        gitHubService = new GitHubService(restTemplate);
    }

    @Test
    void resultListIsNotEmpty() {
        // given
        String name = "mciapski";
        List<GitHubRepositoriesDto> testList = gitHubService.getAllUserRepositories(name);
        // then expect
        assertThat(testList).isNotNull();
    }

    @Test
    void shouldPassWhenContentTypeIsApplicationJson() throws Exception {
        this.mockMvc.perform(get("/github-repository")
                        .contentType("application/json")
                        .param("name","mciapski"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotPassWhenContentTypeIsNotApplicationJson() throws Exception {
        this.mockMvc.perform(get("/github-repository")
                        .contentType("application/xml")
                        .accept("application/xml")
                        .param("name","mciapski"))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void receiveOnlyNotForkedRepositories() {
        // given
        GitHubRepositoriesDto firstElement = new GitHubRepositoriesDto("test1", new GitHubRepositoriesDto.Owner("test1"), true);
        GitHubRepositoriesDto secondElement = new GitHubRepositoriesDto("test2", new GitHubRepositoriesDto.Owner("test2"), false);
        List<GitHubRepositoriesDto> allReposList = List.of(firstElement, secondElement);
        // when
        List<GitHubRepositoriesDto> expectedList = gitHubService.getAllNotForkedRepositories(allReposList);
        // then
        assertThat(expectedList).isEqualTo(List.of(new GitHubRepositoriesDto("test2", new GitHubRepositoriesDto.Owner("test2"), false)));
    }

}