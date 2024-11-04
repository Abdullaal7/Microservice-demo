package com.example.election.service;

import com.example.election.entity.Election;
import com.example.election.repository.ElectionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ElectionService {

private ElectionRepository electionRepository;

    // Get all elections
    public List<Election> getAllElections() {
        return electionRepository.findAll();
    }

    // Get election by ID
    public Election getElectionById(Integer id) {
        return electionRepository.findById(id).orElse(null);
    }

    // Add a new election
    public Election addElection(Election election) {
        return electionRepository.save(election);
    }

    // Update an election
    public Election updateElection(Integer id, Election updatedElection) {
        Election existingElection = electionRepository.findById(id).orElse(null);
        if (existingElection != null) {
            existingElection.setName(updatedElection.getName());
            existingElection.setStartDate(updatedElection.getStartDate());
            existingElection.setEndDate(updatedElection.getEndDate());
            existingElection.setDescription(updatedElection.getDescription());
            return electionRepository.save(existingElection);
        }
        return null;
    }

    // Delete an election
    public void deleteElection(Integer id) {
        electionRepository.deleteById(id);
    }
}


