package com.example.vote.service;

import com.example.vote.entity.Vote;
import com.example.vote.entity.VotePin;
import com.example.vote.repository.VotePinRepository;
import com.example.vote.repository.VoteRepository;
import com.example.vote.response.CandidateResponse;
import com.example.vote.response.ElectionDefinitionResponse;
import com.example.vote.response.PositionResponse;
import com.example.vote.response.VoterResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class VoteService {
    private final VotePinRepository votePinRepository;
    private final VoteRepository voteRepository;
    private final RestTemplate restTemplate;

    private final String voterServiceUrl = "http://voter-service/voter/";
    private final String electionDefinitionServiceUrl = "http://election-definition-service/elections/";
    private final String positionServiceUrl = "http://position-service/positions/";
    private final String candidateServiceUrl = "http://candidate-service/candidates/";

    // Helper method to generate 4-digit unique PIN code
    private String generateUniquePinCode() {
        Random random = new Random();
        String pinCode;
        do {
            pinCode = String.format("%04d", random.nextInt(10000));
        } while (votePinRepository.findByPinCode(pinCode).isPresent());
        return pinCode;
    }

    // Generate unique 4-digit pin code for an election
    public String generatePinCodeForElection(Integer electionId) {
        String pinCode = generateUniquePinCode();
        VotePin votePin = new VotePin();
        votePin.setPinCode(pinCode);
        votePin.setElectionId(electionId);
        votePinRepository.save(votePin);
        return pinCode;
    }

    // Add a vote with validation
    public void saveVote(Vote vote, String pinCode) {
        // Check if pinCode exists and is not used
        Optional<VotePin> votePinOpt = votePinRepository.findByPinCode(pinCode);
        if (votePinOpt.isPresent()) {
            VotePin votePin = votePinOpt.get();
            if (!votePin.getVoted()) {
                // Perform all validations and fetch related data
                validateAndFetchData(vote);

                // Set Pin-code
                vote.setPinCode(pinCode);

                // Save the vote
                voteRepository.save(vote);

                // Mark the pin as used
                votePin.setVoted(true);
                votePinRepository.save(votePin);
            } else {
                throw new IllegalArgumentException("Pin code has already been used.");
            }
        } else {
            throw new IllegalArgumentException("Invalid pin code.");
        }
    }

    // Helper to validate and fetch election, position, candidate, and voter
    private void validateAndFetchData(Vote vote) {
        // Validate and fetch election
        ElectionDefinitionResponse election = restTemplate.getForObject(electionDefinitionServiceUrl + vote.getElectionId(), ElectionDefinitionResponse.class);
        if (election == null) {
            throw new IllegalArgumentException("Invalid Election ID.");
        }
        validateElectionExpiry(election);

        // Validate and fetch voter
        VoterResponse voter = restTemplate.getForObject(voterServiceUrl + vote.getVoterId(), VoterResponse.class);
        if (voter == null) {
            throw new IllegalArgumentException("Invalid Voter ID.");
        }
        if (hasVoterAlreadyVoted(vote.getVoterId(), vote.getElectionId())) {
            throw new IllegalArgumentException("Voter has already cast a vote in this election.");
        }

        // Validate and fetch candidate
        CandidateResponse candidate = restTemplate.getForObject(candidateServiceUrl + vote.getCandidateId(), CandidateResponse.class);
        if (candidate == null) {
            throw new IllegalArgumentException("Invalid Candidate ID.");
        }
        // Validate if Position belongs to Election
        validatePositionBelongsToElection(vote.getPositionId(), vote.getElectionId());

        // Validate if candidate belongs to position
        validateCandidateBelongsToPosition(candidate, vote.getPositionId());


    }

    // Voter Has Already Voted in the Election
    private boolean hasVoterAlreadyVoted(Integer voterId, Integer electionId) {
        return voteRepository.existsByVoterIdAndElectionId(voterId, electionId);
    }

    // Candidate Belongs to Position and Election
    private void validateCandidateBelongsToPosition(CandidateResponse candidate, Integer positionId) {
        if (!candidate.getPositionId().equals(positionId)) {
            throw new IllegalArgumentException("Candidate does not belong to the selected position.");
        }
    }

    // Validate if Position belongs to Election
    private void validatePositionBelongsToElection(Integer positionId, Integer electionId) {
        // Fetch positions for the election
        String url = positionServiceUrl + "by-election/" + electionId;
        PositionResponse[] positions = restTemplate.getForObject(url, PositionResponse[].class);

        if (positions == null || positions.length == 0) {
            throw new IllegalArgumentException("No positions found for the given election.");
        }

        // Check if positionId is in the list of positions for the election
        boolean positionExists = false;
        for (PositionResponse position : positions) {
            if (position.getId().equals(positionId)) {
                positionExists = true;
                break;
            }
        }

        if (!positionExists) {
            throw new IllegalArgumentException("The position does not belong to the specified election.");
        }
    }

    // Election Expiration Validation
    private void validateElectionExpiry(ElectionDefinitionResponse election) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate endDate = LocalDate.parse(election.getEndDate(), formatter);

        if (endDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Election has expired and is no longer accepting votes.");
        }
    }

    public Integer countVotes(Integer electionId, Integer positionId, Integer candidateId) {
        return voteRepository.countByElectionIdAndPositionIdAndCandidateId(electionId, positionId, candidateId);
    }

    // Get all votes for positions by election
//    public List<Vote> getVotesByElectionAndPosition(Integer electionId, Integer positionId) {
//        return voteRepository.findByElectionIdAndPositionId(electionId, positionId);
//    }

    // Get all votes by candidate
//    public List<Vote> getVotesByCandidate(Integer candidateId) {
//        return voteRepository.findByCandidateId(candidateId);
//    }
}