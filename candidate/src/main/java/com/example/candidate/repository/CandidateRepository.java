package com.example.candidate.repository;

import com.example.candidate.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {

    List<Candidate> findByElectionDefinitionIdAndPositionId(Integer electionDefinitionId, Integer positionId);

    long countByElectionDefinitionIdAndPositionId(Integer electionDefinitionId, Integer positionId);
}

