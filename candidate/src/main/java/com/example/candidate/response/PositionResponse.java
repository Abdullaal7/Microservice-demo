package com.example.candidate.response;

import lombok.Data;

@Data
public class PositionResponse {

    private Integer id;
    private Integer version;
    private String name;
    private Integer electionDefinitionId;
}
