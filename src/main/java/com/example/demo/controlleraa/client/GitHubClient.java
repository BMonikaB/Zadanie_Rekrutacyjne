package com.example.demo.controlleraa.client;

import com.example.demo.controlleraa.model.BranchGit;
import com.example.demo.controlleraa.model.RepositoryGit;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;


import java.util.List;
@RegisterRestClient(baseUri = "https://api.github.com")
public interface GitHubClient {
    @GET
    @Path("/users/{username}/repos")
    Uni<List<RepositoryGit>> getUserRepositories(@PathParam("username") String username);

    @GET
    @Path("/repos/{owner}/{repo}/branches")
    Uni<List<BranchGit>> getBranches(@PathParam("owner") String owner, @PathParam("repo") String repo);
}