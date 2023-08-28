1. Application consumes GitHub API,
2. It is using one endpoint in controller: "/github-repository". And as parameters user is providing name of repository owner and proper header content type.
3. If content type will be different from "application/json" application will throw an BadHeaderException and http response will  be 406 with status and message fields included as a custom json,
4. If name of user will be incorrect then resources won't be possible to receive and application will throw customized 404 HttpClientErrorException with status and message fields,
5. If content type and name will be correct then method getAllUserRepositories() collect all repositories per user,
6. After this method getAllNotForkedRepositories() will filter them in order to gather not forked repositories,
7. Method getAllBranchesForRepository() allowes to gather all branches and last commit per one repository,
8. Method getAllBranchesForRepository() is used in method mapToEntityObject() in order to get all branches for all repositories in loop,
9. Last step is method mapToEntityObject(). It allowes to create objects GitHubEntity and collect them into list.
10. Endpoint  "/github-repository" is presenting list from step 9.