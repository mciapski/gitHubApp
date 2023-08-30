## gitHubApp
Application consumes GitHub API: https://api.github.com/users/USERNAME/repos in order to receive all repositories which are not forked by user and "https://api.github.com/repos/USERNAME/REPO/branches" to gather all branches.
As a result of gitHubApp we should get API presenting:
User Name,
Repository Name,
All branches for this Repository and last commit.

## Controller and parameters
```java
    @RequestMapping(value = "/github-repository", method = RequestMethod.GET)
    public List<GitHubEntity> getGitHubEntityList(@RequestHeader("Content-Type") String contentType,
                                                  @RequestParam(value = "name") String name) {
        if (!contentType.equals("application/json")) {
            throw new BadHeaderException();
        }
        return gitHubService.mapToEntityObject(gitHubService.getAllNotForkedRepositories(gitHubService.getAllUserRepositories(name)));
    }

Endpoint "/github-repository" requires two parameters from user: header content-type and owner name.

If content-type is different then "application/json" user will receive HTTP 406 and exception BadHeaderException will look like:
{
    "status": "406 NOT_ACCEPTABLE",
    "message": "Wrong header received"
}

If owner name will be incorrect then user will receive HTTP 404 and HttpClientErrorException will looks like:
{
    "status": "404 NOT_FOUND",
    "message": "Wrong owner name"
}
```
## GitHubService

### getAllUserRepositories
```java
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

And DTO:

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
```
getAllUserRepositories() as name is telling is gathering all repositories by one user. Parameter for this method is "name" received in controller.

### getAllNotForkedRepositories

```java
    public List<GitHubRepositoriesDto> getAllNotForkedRepositories(List<GitHubRepositoriesDto> allRepos) {
        if (Objects.equals(allRepos, null)) {
            throw new IllegalArgumentException(" Input list of repositories is null");
        }
        if (allRepos.isEmpty()) {
            throw new IllegalArgumentException(" Input list of repositories is empty");
        }

        return allRepos.stream().filter(element -> !element.isFork()).collect(Collectors.toList());
    }
```
getAllNotForkedRepositories() is method to filter only not forked repositories.

## getAllBranchesForRepository

```java
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

And DTO:

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
```
In this step getAllBranchesForRepository() is creating list of branches by repository.

## mapToEntityObject

Next step is to create GitHubEntity object by mapping previous dtos.
```java
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
```
By using setters a new object is created. GitHubEntity class looks like below:

```java
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
```
