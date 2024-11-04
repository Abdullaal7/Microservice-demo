package com.example.vote.repository;

import com.example.vote.entity.VotePin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotePinRepository extends JpaRepository<VotePin, Integer> {
    Optional<VotePin> findByPinCode(String pinCode);


}
