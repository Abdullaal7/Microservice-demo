package com.example.candidate.controller;

import com.example.candidate.entity.Candidate;
import com.example.candidate.service.CandidateService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/candidates")
@AllArgsConstructor
public class CandidateController {
    private CandidateService candidateService;

    @GetMapping
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        return  ResponseEntity.ok(candidateService.getAll());
    }

    // GET candidate by ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Candidate>> getCandidateById(@PathVariable Integer id) {
        return  ResponseEntity.ok(candidateService.getById(id));
    }

    // GET candidates by electionDefinitionId and positionId
    @GetMapping("/filter")
    public ResponseEntity<List<Candidate>> getCandidatesByElectionDefinitionIdAndPosition(
            @RequestParam Integer electionDefinitionId, @RequestParam Integer positionId) {
        return  ResponseEntity.ok(
                candidateService.getCandidatesByElectionDefinitionIdAndPosition(electionDefinitionId, positionId));
    }

    // GET candidate count by electionDefinitionId and positionId
    @GetMapping("/count")
    public ResponseEntity<?> getCandidateCountByElectionDefinitionIdAndPosition(
            @RequestParam Integer electionDefinitionId, @RequestParam Integer positionId) {
        return ResponseEntity.ok(
                candidateService.getCandidateCountByElectionDefinitionIdAndPosition(electionDefinitionId, positionId));
    }

    // POST create new candidate
    // Create a new candidate
    @PostMapping
    public ResponseEntity<Candidate> createCandidate(@RequestParam Integer electionId,
                                                     @RequestParam Integer positionId,
                                                     @RequestBody Candidate candidate) {

        return ResponseEntity.ok(candidateService.save(electionId, positionId , candidate));
    }


    // Update an existing candidate
    @PutMapping("/{id}")
    public ResponseEntity<Candidate> updateCandidate(@PathVariable Integer id,
                                                     @RequestParam Integer electionId,
                                                     @RequestParam Integer positionId,
                                                     @RequestBody Candidate updatedCandidate) {
            return  ResponseEntity.ok(candidateService.update(id , electionId , positionId , updatedCandidate));
    }

    // DELETE candidate
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Integer id) {
        candidateService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
