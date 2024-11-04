package com.example.vote.response;


import lombok.Data;

@Data
public class CandidateResponse {

    private Integer id;
    private Integer version;
    private String name;
    private String description;
    private Integer positionId;
    private Integer electionDefinitionId;
}
