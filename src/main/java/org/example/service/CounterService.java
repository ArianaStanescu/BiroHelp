package org.example.service;

import org.example.entity.Counter;
import org.example.repositories.CounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CounterService {

    @Autowired
    private CounterRepository counterRepository;

    public List<Counter> findAllCounters() {
        return counterRepository.findAll();
    }

    public Optional<Counter> findById(Long id) {
        return counterRepository.findById(id);
    }

    public Counter save(Counter counter) {
        return counterRepository.save(counter);
    }

    public Optional<Counter> update(Long id, Counter counter) {
        if (counterRepository.existsById(id)) {
            counter.setId(id);
            return Optional.of(counterRepository.save(counter));
        }
        return Optional.empty();
    }

    public void delete(Long id) {
        counterRepository.deleteById(id);
    }
}
