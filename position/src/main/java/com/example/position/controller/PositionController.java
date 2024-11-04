package com.example.position.controller;

import com.example.position.entity.Position;
import com.example.position.service.PositionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/positions")
@AllArgsConstructor
public class PositionController {

    private PositionService positionService;

    // Get all positions
    @GetMapping
    public ResponseEntity<List<Position>> getAllPositions() {
        return ResponseEntity.ok(positionService.getAllPositions());
    }

    // Get positions by election definition
    @GetMapping("/by-election/{electionDefinitionId}")
    public ResponseEntity<List<Position>> getPositionsByElectionDefinition(@PathVariable Integer electionDefinitionId) {
        return ResponseEntity.ok(positionService.getPositionsByElectionDefinition(electionDefinitionId));
    }

    // Get a position by ID
    @GetMapping("/{id}")
    public ResponseEntity<Position> getPositionById(@PathVariable Integer id) {
        return ResponseEntity.ok(positionService.getPositionById(id));
    }

    // Add a new position (with electionDefinitionId as part of the request)
    @PostMapping
    public ResponseEntity<Position> addPosition(@RequestBody Position position, @RequestParam Integer electionDefinitionId) {
        return ResponseEntity.ok(positionService.addPosition(position, electionDefinitionId));
    }

    // Update a position (with electionDefinitionId as part of the request)
    @PutMapping("/{id}")
    public ResponseEntity<Position> updatePosition(@PathVariable Integer id, @RequestBody Position position, @RequestParam Integer electionDefinitionId) {
        return ResponseEntity.ok(positionService.updatePosition(id, position, electionDefinitionId));
    }

    // Delete a position
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable Integer id) {
        positionService.deletePosition(id);
        return ResponseEntity.noContent().build();
    }
}
