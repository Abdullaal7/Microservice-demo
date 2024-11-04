package com.example.vote.controller;

import com.example.vote.entity.Vote;
import com.example.vote.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votes")
@AllArgsConstructor
public class VoteController {

    private final VoteService voteService;

    //Generate pin code for an election
    @PostMapping("/generate-pin")
    public ResponseEntity<String> generatePinCodeForElection(@RequestParam Integer electionId) {
        String pinCode = voteService.generatePinCodeForElection(electionId);
        return ResponseEntity.ok(pinCode);
    }

    //Cast a vote using pin code and validate all required fields
    @PostMapping
    public ResponseEntity<String> castVote(@RequestParam String pinCode, @RequestBody Vote vote) {
        voteService.saveVote(vote, pinCode);
        return ResponseEntity.ok("Vote successfully cast.");
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getVoteCount(
            @RequestParam Integer electionId,
            @RequestParam Integer positionId,
            @RequestParam Integer candidateId) {
        Integer voteCount = voteService.countVotes(electionId, positionId, candidateId);
        return new ResponseEntity<>(voteCount, HttpStatus.OK);
    }

    //Get all votes for positions by election
//    @GetMapping("/by-election-and-position")
//    public ResponseEntity<List<Vote>> getVotesByElectionAndPosition(@RequestParam Integer electionId, @RequestParam Integer positionId) {
//        List<Vote> votes = voteService.getVotesByElectionAndPosition(electionId, positionId);
//        return ResponseEntity.ok(votes);
//    }

//    //Get all votes by candidate
//    @GetMapping("/by-candidate")
//    public ResponseEntity<List<Vote>> getVotesByCandidate(@RequestParam Integer candidateId) {
//        List<Vote> votes = voteService.getVotesByCandidate(candidateId);
//        return ResponseEntity.ok(votes);
//    }
}
