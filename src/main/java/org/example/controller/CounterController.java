package org.example.controller;

import org.example.entity.Counter;
import org.example.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/counters")
public class CounterController {

    @Autowired
    private CounterService counterService;

    // Obține toate ghișeele
    @GetMapping
    public List<Counter> getAllCounters() {
        return counterService.findAllCounters();
    }

    // Obține un ghișeu după ID
    @GetMapping("/{id}")
    public ResponseEntity<Counter> getCounterById(@PathVariable Long id) {
        return counterService.findById(id)
                .map(counter -> ResponseEntity.ok(counter))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Salvează un ghișeu nou
    @PostMapping
    public ResponseEntity<Counter> createCounter(@RequestBody Counter counter) {
        Counter savedCounter = counterService.save(counter);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCounter);
    }

    // Actualizează un ghișeu
    @PutMapping("/{id}")
    public ResponseEntity<Counter> updateCounter(@PathVariable Long id, @RequestBody Counter counter) {
        return counterService.update(id, counter)
                .map(updatedCounter -> ResponseEntity.ok(updatedCounter))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Șterge un ghișeu
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCounter(@PathVariable Long id) {
        counterService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
