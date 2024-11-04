package com.example.voter.service;

import com.example.voter.entity.Voter;
import com.example.voter.repository.VoterRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VoterService {
private VoterRepository voterRepository;

//    @CircuitBreaker(name = "voterService", fallbackMethod = "fallbackMethod")
//    public List<Voter> getAll (){
//        return voterRepository.findAll();
//    }


    // Circuit breaker for getAll method with fallback
    @CircuitBreaker(name = "voter-service", fallbackMethod = "fallbackMethod")
    public List<Voter> getAll() {
        throw new RuntimeException("Simulated service failure!");
    }

    // Fallback method for circuit breaker
    public List<Voter> fallbackMethod(Throwable throwable) {
        System.out.println("Fallback triggered: " + throwable.getMessage());
        return Collections.emptyList(); // Returning an empty list as a fallback
    }



    public Optional<Voter> getById(Integer id){
        return voterRepository.findById(id);
    }


    public Voter save (Voter voter){
        return voterRepository.save(voter);
    }

    public Voter update(Integer id, Voter voter) {
        Voter existingVoter = voterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Voter not found with id: " + id));

        // Update fields with the values from the incoming voter object
        existingVoter.setName(voter.getName());
        existingVoter.setNationalId(voter.getNationalId());
        existingVoter.setEmail(voter.getEmail());
        existingVoter.setPhone(voter.getPhone());

        return voterRepository.save(existingVoter);
    }

    public void delete (Integer id){
        Voter voter = voterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Voter not found with id: " + id));
        voterRepository.delete(voter);
    }

}
