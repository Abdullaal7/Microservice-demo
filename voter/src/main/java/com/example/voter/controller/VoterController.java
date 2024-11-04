package com.example.voter.controller;

import com.example.voter.entity.Voter;
import com.example.voter.service.VoterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/voter")
@AllArgsConstructor
public class VoterController {
    private VoterService voterService;

    @GetMapping
    public List<Voter> getAll (){
        return voterService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getByID (@PathVariable (value = "id") Integer id){
        return ResponseEntity.ok(voterService.getById(id));
    }

    @PostMapping
    public Voter save (@RequestBody Voter voter){
        return voterService.save(voter);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update (@PathVariable Integer id ,@RequestBody Voter voter){
        voterService.update(id,voter);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Integer id){
        voterService.delete(id);
        return ResponseEntity.ok().build();
    }
}
