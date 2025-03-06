package com.example.demo.controlleraa.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class RepositoryGit {

    public String name;
    private Boolean fork;
    private Owner owner;
    public String ownerLogin;
    public List<BranchGit> branchGits;

    public String getOwnerLogin() {
        return owner != null ? owner.getLogin() : null;
    }
}