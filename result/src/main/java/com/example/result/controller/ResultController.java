package com.example.result.controller;

import com.example.result.dto.ResultDTO;
import com.example.result.service.ResultService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/results")
@AllArgsConstructor
public class ResultController {
    private final ResultService resultService;

    // GET /results - Get election results
    @GetMapping
    public ResponseEntity<List<ResultDTO>> getResults(
            @RequestParam Integer electionId,
            @RequestParam Integer positionId) {
        return new ResponseEntity<>(resultService.getResults(electionId, positionId), HttpStatus.OK);
    }
}