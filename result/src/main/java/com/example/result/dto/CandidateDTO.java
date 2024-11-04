package com.example.result.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CandidateDTO {

    private Integer id;
    private Integer version;
    private String name;
    private String description;
    private Integer positionId;
    private Integer electionDefinitionId;
}
