package com.example.result.service;

import com.example.result.dto.ResultDTO;
import com.example.result.dto.CandidateDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class ResultService {
    private final RestTemplate restTemplate;

    public List<ResultDTO> getResults(Integer electionId, Integer positionId) {
        CandidateDTO[] candidates;
        try {
            // Use CircuitBreaker on fetchCandidatesWithCircuitBreaker
            candidates = fetchCandidatesWithCircuitBreaker(electionId, positionId);
        } catch (IllegalStateException e) {
            // Handle the "No instances available" case by calling fallback manually
            return fallbackForFetchCandidates(electionId, positionId, e);
        }

        if (candidates == null || candidates.length == 0) {
            throw new IllegalArgumentException("No candidates found for this election and position.");
        }

        List<ResultDTO> results = new ArrayList<>();

        for (CandidateDTO candidate : candidates) {
            Integer votes;
            try {
                // Use CircuitBreaker on fetchVoteCountWithCircuitBreaker
                votes = fetchVoteCountWithCircuitBreaker(electionId, positionId, candidate.getId());
            } catch (IllegalStateException e) {
                // Handle the "No instances available" case by calling fallback manually
                votes = fallbackForFetchVoteCount(electionId, positionId, candidate.getId(), e);
            }

            results.add(new ResultDTO(electionId, positionId, candidate.getId(), votes != null ? votes : 0));
        }

        results.sort(Comparator.comparing(ResultDTO::getVotes).reversed());
        return results;
    }

    @CircuitBreaker(name = "candidateServiceCircuitBreaker", fallbackMethod = "fallbackForFetchCandidates")
    private CandidateDTO[] fetchCandidatesWithCircuitBreaker(Integer electionId, Integer positionId) {
        String candidateServiceUrl = "http://candidate-service/candidates/filter?electionDefinitionId=" + electionId + "&positionId=" + positionId;
        return restTemplate.getForObject(candidateServiceUrl, CandidateDTO[].class);
    }

    // Fallback method for fetchCandidates
    public List<ResultDTO> fallbackForFetchCandidates(Integer electionId, Integer positionId, Throwable t) {
        System.err.println("Candidate service is down or unavailable: " + t.getMessage());
        return List.of(); // Return an empty list or default response when the candidate service is unavailable
    }

    @CircuitBreaker(name = "voteServiceCircuitBreaker", fallbackMethod = "fallbackForFetchVoteCount")
    private Integer fetchVoteCountWithCircuitBreaker(Integer electionId, Integer positionId, Integer candidateId) {
        String voteServiceUrl = "http://vote-service/votes/count?electionId=" + electionId + "&positionId=" + positionId + "&candidateId=" + candidateId;
        return restTemplate.getForObject(voteServiceUrl, Integer.class);
    }

    // Fallback method for fetchVoteCount
    public Integer fallbackForFetchVoteCount(Integer electionId, Integer positionId, Integer candidateId, Throwable t) {
        System.err.println("Vote service is down or unavailable: " + t.getMessage());
        return 0; // Default to 0 votes as a fallback
    }
}
