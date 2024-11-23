package org.example.controller;

import org.example.dto.CounterDto;
import org.example.dto.DocumentCreateDto;
import org.example.dto.DocumentDto;
import org.example.entity.Counter;
import org.example.entity.Document;
import org.example.mapper.CounterMapper;
import org.example.mapper.DocumentMapper;
import org.example.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/counters")
public class CounterController {

    @Autowired
    private CounterService counterService;

    @Autowired
    private CounterMapper counterMapper;

    // Obține toate ghișeele
    @GetMapping
    public ResponseEntity<List<CounterDto>> getAllCounters() {

        List<Counter> counters = counterService.findAllCounters();

        List<CounterDto> CounterDtos = counters.stream()
                .map(counterMapper::mapCounterToCounterDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CounterDtos);
    }

    // Obține un ghișeu după ID
    @GetMapping("/{id}")
    public ResponseEntity<CounterDto> getCounterById(@PathVariable Long id) {

        Counter counter = counterService.findById(id).orElse(null);

        if (counter != null) {
            return ResponseEntity.ok(counterMapper.mapCounterToCounterDto(counter));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PostMapping("/{officeId}")
    public ResponseEntity<CounterDto> createCounter(@PathVariable Long officeId, @RequestBody CounterDto counterDto) {
        Counter counter = counterMapper.mapCounterDtoToCounter(counterDto);
        Counter savedCounter = counterService.save(counter, officeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(counterMapper.mapCounterToCounterDto(savedCounter));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<CounterDto> partialUpdateCounter(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<Counter> updatedCounter = counterService.partialUpdateCounter(id, updates);

        if (updatedCounter.isPresent()) {
            return ResponseEntity.ok(counterMapper.mapCounterToCounterDto(updatedCounter.get()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCounter(@PathVariable Long id) {
        counterService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
