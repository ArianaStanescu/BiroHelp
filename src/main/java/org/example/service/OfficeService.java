package org.example.service;

import org.example.entity.Office;
import org.example.repositories.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfficeService {

    @Autowired
    private OfficeRepository officeRepository;

    public List<Office> findAllOffices() {
        return officeRepository.findAll();
    }

    public Optional<Office> findById(Long id) {
        return officeRepository.findById(id);
    }

    public Office save(Office office) {
        return officeRepository.save(office);
    }

    public Optional<Office> update(Long id, Office office) {
        if (officeRepository.existsById(id)) {
            office.setId(id);
            return Optional.of(officeRepository.save(office));
        }
        return Optional.empty();
    }

    public void delete(Long id) {
        officeRepository.deleteById(id);
    }
}
