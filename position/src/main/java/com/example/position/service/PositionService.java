package com.example.position.service;

import com.example.position.entity.Position;
import com.example.position.repository.PositionRepository;
import com.example.position.response.ElectionDefinitionResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final RestTemplate restTemplate;
    private final String electionDefinitionServiceUrl = "http://election-definition-service/elections/";

    private static final String SERVICE_NAME = "position-service";

    // Get all positions
    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    // Get a position by ID
    public Position getPositionById(Integer id) {
        return positionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Position not found for ID: " + id));
    }

    // Get positions by election definition
    public List<Position> getPositionsByElectionDefinition(Integer electionDefinitionId) {
        return positionRepository.findByElectionDefinitionId(electionDefinitionId);
    }

    // Add a new position with CircuitBreaker
    @CircuitBreaker(name = SERVICE_NAME, fallbackMethod = "fallbackForElectionService")
    public Position addPosition(Position position, Integer electionDefinitionId) {
        // Validate election definition by calling the election-definition-service
        validateElectionDefinition(electionDefinitionId);

        // Set the election definition ID and save the position
        position.setElectionDefinitionId(electionDefinitionId);
        return positionRepository.save(position);
    }

    // Update an existing position with CircuitBreaker
    @CircuitBreaker(name = SERVICE_NAME, fallbackMethod = "fallbackForElectionService")
    public Position updatePosition(Integer id, Position updatedPosition, Integer electionDefinitionId) {
        // Fetch the existing position by ID
        Position existingPosition = positionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Position not found for ID: " + id));

        // Validate the new election definition before updating
        validateElectionDefinition(electionDefinitionId);

        // Update the position details
        existingPosition.setName(updatedPosition.getName());
        existingPosition.setElectionDefinitionId(electionDefinitionId);

        // Save and return the updated position
        return positionRepository.save(existingPosition);
    }

    // Delete a position
    public void deletePosition(Integer id) {
        positionRepository.deleteById(id);
    }

    // Validate the election definition
    @CircuitBreaker(name = SERVICE_NAME, fallbackMethod = "fallbackForElectionService")
    private void validateElectionDefinition(Integer electionDefinitionId) {
        String url = electionDefinitionServiceUrl + electionDefinitionId;
        ElectionDefinitionResponse response = restTemplate.getForObject(url, ElectionDefinitionResponse.class);

        // Throw an exception if the election definition does not exist
        if (response == null) {
            throw new IllegalArgumentException("Election not found for ID: " + electionDefinitionId);
        }
    }


    // Fallback method for all calls to the election definition service
    public ElectionDefinitionResponse fallbackForElectionService(Integer electionDefinitionId, Throwable throwable) {
        // Handle discovery service not available
        if (throwable instanceof IllegalStateException) {
            System.out.println("Fallback: No instances available for election-definition-service.");
        } else {
            System.out.println("Fallback triggered for Election Definition Service: " + throwable.getMessage());
        }

        // Handle fallback logic here, for example, log the error or return a default response
        return new ElectionDefinitionResponse(); // Return an empty or default object
    }
}