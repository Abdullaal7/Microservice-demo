package com.example.vote.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    @Column()
    private Integer voterId;  // References Voter Service

    @Column()
    private Integer electionId;  // References ElectionDefinition Service

    @Column()
    private Integer positionId;  // References Position Service

    @Column()
    private Integer candidateId;  // References Candidate Service

    @Column(unique = true)
    private String pinCode;
}
