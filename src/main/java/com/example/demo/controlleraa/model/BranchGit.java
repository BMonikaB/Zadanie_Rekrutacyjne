package com.example.demo.controlleraa.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BranchGit {
    private String name;
    private String lastCommitSha;
}