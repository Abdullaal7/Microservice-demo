package com.example.candidate.service;

import com.example.candidate.entity.Candidate;
import com.example.candidate.repository.CandidateRepository;
import com.example.candidate.response.ElectionDefinitionResponse;
import com.example.candidate.response.PositionResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;

    @Autowired
    private final RestTemplate restTemplate;

    private final String positionServiceUrl = "http://position-service/positions/";
    private final String electionDefinitionServiceUrl = "http://election-definition-service/elections/";

    // Helper method to validate positionId from Position Service
    private void validatePositionId(Integer positionId) {
        String url = positionServiceUrl + positionId;
        PositionResponse response = restTemplate.getForObject(url, PositionResponse.class);

        // Throw an exception if the election definition does not exist
        if (response == null) {
            throw new IllegalArgumentException("Election not found for ID: " + positionId);
        }
    }

    // Helper method to validate electionDefinitionId from Election Definition Service
    private void validateElectionDefinitionId(Integer electionDefinitionId) {
        String url = electionDefinitionServiceUrl + electionDefinitionId;
        ElectionDefinitionResponse response = restTemplate.getForObject(url, ElectionDefinitionResponse.class);

        // Throw an exception if the election definition does not exist
        if (response == null) {
            throw new IllegalArgumentException("Election not found for ID: " + electionDefinitionId);
        }
    }

    // Create a new candidate
    public Candidate save(Integer electionId, Integer positionId ,Candidate candidate) {
        validatePositionId(positionId);
        validateElectionDefinitionId(electionId);
        return candidateRepository.save(candidate);
    }

    // Update an existing candidate
    public Candidate update(Integer id,Integer electionId, Integer positionId , Candidate updatedCandidate) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found with ID: " + id));
        validatePositionId(positionId);
        validateElectionDefinitionId(electionId);

        candidate.setName(updatedCandidate.getName());
        candidate.setPositionId(updatedCandidate.getPositionId());
        candidate.setElectionDefinitionId(updatedCandidate.getElectionDefinitionId());
        candidate.setDescription(updatedCandidate.getDescription());
        return candidateRepository.save(candidate);
    }

    // Get all candidates
    public List<Candidate> getAll() {
        return candidateRepository.findAll();
    }

    // Get candidate by ID
    public Optional<Candidate> getById(Integer id) {
        return candidateRepository.findById(id);
    }

    // Delete a candidate by ID
    public void delete(Integer id) {
        candidateRepository.deleteById(id);
    }

    // Get all candidates by election definition and position
    public List<Candidate> getCandidatesByElectionDefinitionIdAndPosition(Integer electionDefinitionId, Integer positionId) {
        // Validate both election and position before retrieving candidates
        validatePositionId(positionId);
        validateElectionDefinitionId(electionDefinitionId);
        return candidateRepository.findByElectionDefinitionIdAndPositionId(electionDefinitionId, positionId);
    }

    // Get count of candidates by election definition and position
    public ResponseEntity<?> getCandidateCountByElectionDefinitionIdAndPosition(Integer electionDefinitionId, Integer positionId) {
        // Validate both election and position before counting candidates
        validatePositionId(positionId);
        validateElectionDefinitionId(electionDefinitionId);
        return ResponseEntity.ok( candidateRepository.countByElectionDefinitionIdAndPositionId(electionDefinitionId, positionId));
    }
}