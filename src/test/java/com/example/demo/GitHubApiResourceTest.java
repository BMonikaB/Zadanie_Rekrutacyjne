package com.example.demo;

import com.example.demo.controlleraa.client.GitHubClient;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import io.smallrye.mutiny.Uni;
import javax.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import java.util.List;
import com.example.demo.controlleraa.model.RepositoryGit;
import com.example.demo.controlleraa.model.BranchGit;
import com.example.demo.controlleraa.controller.GitHubApiResource;

import static org.assertj.core.api.Assertions.assertThat;
import jakarta.ws.rs.core.Response;


@QuarkusTest
public class GitHubApiResourceTest {

    @Inject
    GitHubApiResource gitHubApiResource;

    @Inject
    @RestClient
    GitHubClient gitHubClient;

    @Test
    public void testGetRepositoriesHappyPath() {
        //użytkownik z istniejącymi repozytoriami nie-forkaii
        String username = "valid-user";  // Zmień

        Uni<Response> responseUni = gitHubApiResource.getRepositories(username);
        Response response = responseUni.await().indefinitely();

        //  czy 200
        assertThat(response.getStatus()).isEqualTo(200);

        List<RepositoryGit> repos = (List<RepositoryGit>) response.getEntity();
        assertThat(repos).isNotNull();
        assertThat(repos.size()).isGreaterThan(0);

        //czy repozytoria są "nie-forkami"
        for (RepositoryGit repo : repos) {
            assertThat(repo.getFork()).isEqualTo(false);
            assertThat(repo.getBranchGits()).isNotNull();

            //czy każda gałąź ma nazwę i Skha ostatniego commita
            for (BranchGit branch : repo.getBranchGits()) {
                assertThat(branch.getName()).isNotEmpty();
                assertThat(branch.getLastCommitSha()).isNotEmpty();
            }
        }
    }
}