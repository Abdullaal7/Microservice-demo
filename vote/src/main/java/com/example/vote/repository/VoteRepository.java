package com.example.vote.repository;

import com.example.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    List<Vote> findByElectionIdAndPositionId(Integer electionId, Integer positionId);

    List<Vote> findByCandidateId(Integer candidateId);

    boolean existsByVoterIdAndElectionId(Integer voterId, Integer electionId);

    Integer countByElectionIdAndPositionIdAndCandidateId(Integer electionId, Integer positionId, Integer candidateId);


}
