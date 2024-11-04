package com.example.election.controller;

import com.example.election.entity.Election;
import com.example.election.service.ElectionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/elections")
@AllArgsConstructor
public class ElectionController {

    private ElectionService electionService;

    // Get all elections
    @GetMapping
    public List<Election> getAllElections() {
        return electionService.getAllElections();
    }

    // Get an election by ID
    @GetMapping("/{id}")
    public Election getElectionById(@PathVariable Integer id) {
        return electionService.getElectionById(id);
    }

    // Add a new election
    @PostMapping
    public Election addElection(@RequestBody Election election) {
        return electionService.addElection(election);
    }

    // Update an election
    @PutMapping("/{id}")
    public Election updateElection(@PathVariable Integer id, @RequestBody Election election) {
        return electionService.updateElection(id, election);
    }

    // Delete an election
    @DeleteMapping("/{id}")
    public void deleteElection(@PathVariable Integer id) {
        electionService.deleteElection(id);
    }
}

