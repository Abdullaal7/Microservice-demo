package com.example.result.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultDTO {
    private Integer electionId;
    private Integer positionId;
    private Integer candidateId;
    private Integer votes;
}
