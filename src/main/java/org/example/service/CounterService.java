package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.entity.Counter;
import org.example.entity.Office;
import org.example.repositories.CounterRepository;
import org.example.repositories.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CounterService {

    @Autowired
    private CounterRepository counterRepository;

    @Autowired
    private OfficeRepository officeRepository;

    public List<Counter> findAllCounters() {
        return counterRepository.findAll();
    }

    public Optional<Counter> findById(Long id) {
        return counterRepository.findById(id);
    }

    public Counter save(Counter counter, Long officeId) {
        Office office = officeRepository.findById(officeId)
                .orElseThrow(() -> new EntityNotFoundException("Office not found"));
        office.addCounters(counter);
        officeRepository.save(office);
        return counter;
    }

    public Optional<Counter> partialUpdateCounter(Long id, Map<String, Object> updates) {
        Optional<Counter> optionalCounter = counterRepository.findById(id);

        if (optionalCounter.isPresent()) {
            Counter counter = optionalCounter.get();
            boolean invalidField = false;

            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if ("name".equals(key)) {
                    counter.setName((String) value);
                }  else {
                    invalidField = true;
                    System.out.println("Invalid field: " + key);
                }
            }

            if (invalidField) {
                return Optional.empty();
            }

            return Optional.of(counterRepository.save(counter));
        }

        return Optional.empty();
    }



    public void delete(Long id) {
        counterRepository.deleteById(id);
    }
}
