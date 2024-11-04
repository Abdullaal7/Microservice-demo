package com.example.candidate.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String name;


    @Column()
    private String description;

    @Column()
    private Integer positionId;  // Reference to Position Service (will validate dynamically)

    @Column()
    private Integer electionDefinitionId;  // Reference to Election Definition Service (will validate dynamically)

}

