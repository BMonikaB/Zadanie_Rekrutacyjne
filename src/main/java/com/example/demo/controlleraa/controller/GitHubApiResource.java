package com.example.demo.controlleraa.controller;

import com.example.demo.controlleraa.client.GitHubClient;
import com.example.demo.controlleraa.model.RepositoryGit;
import com.example.demo.controlleraa.error.ErrorResponse;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
//import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/github")
public class GitHubApiResource {

    @Inject
    @RestClient
    GitHubClient gitHubClient;

    @GET
    @Path("/repositories")
    public Uni<Response> getRepositories(@QueryParam("username") String username) {
        return fetchRepositories(username)
                .onItem().transformToUni(repositories -> {
                    if (repositories == null || repositories.isEmpty()) {
                        return buildErrorResponse("User not found");
                    }
                    return processRepositories(repositories);
                });
    }

    private Uni<List<RepositoryGit>> fetchRepositories(String username) {
        return gitHubClient.getUserRepositories(username);
    }

    private Uni<Response> buildErrorResponse(String message) {
        return Uni.createFrom().item(
                Response.status(404)
                        .entity(new ErrorResponse(404, message))
                        .build()
        );
    }

    private Uni<Response> processRepositories(List<RepositoryGit> repositories) {
        List<Uni<RepositoryGit>> repositoryUnis = repositories.stream()
                .filter(repo -> !repo.getFork()) //nie są forkami
                .map(this::addBranchesToRepository) // sla każdego repozytorium pobieramy gałęzie
                .collect(Collectors.toList());

        return combineRepositories(repositoryUnis);
    }

    private Uni<Response> combineRepositories(List<Uni<RepositoryGit>> repositoryUnis) {
        // Łączymy wszystkie Uni z repozytoriami i gałęziami
        return Uni.combine().all().unis(repositoryUnis)
                .withUni(results -> {
                    List<RepositoryGit> reposWithBranches = (List<RepositoryGit>) results;
                    // Zwracamy odpowiedź HTTP
                    return Uni.createFrom().item(Response.ok(reposWithBranches).build());
                });
    }

    private Uni<RepositoryGit> addBranchesToRepository(RepositoryGit repo) {
        // gałęzie dla repo
        return gitHubClient.getBranches(repo.getOwnerLogin(), repo.getName())
                .onItem().transform(branches -> {
                    repo.setBranchGits(branches);
                    return repo;
                });
    }
}
