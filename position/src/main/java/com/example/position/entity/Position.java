package com.example.position.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column
    private Integer electionDefinitionId;  // This will be fetched from the Election Definition Service
}
