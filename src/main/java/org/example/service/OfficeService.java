package org.example.service;

import io.micrometer.observation.ObservationFilter;
import org.example.entity.Office;
import org.example.repositories.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.lang.reflect.Field;
import java.util.Map;

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


    public Optional<Office> partialUpdate(Long id, Map<String, Object> updates) {
        Optional<Office> optionalOffice = officeRepository.findById(id);

        if (optionalOffice.isPresent()) {
            Office office = optionalOffice.get();

            boolean invalidField = false;

            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if ("name".equals(key)) {
                    office.setName((String) value);
                }  else {
                    invalidField = true;
                }
            }

            if (invalidField) {
                return Optional.empty();
            }

            return Optional.of(officeRepository.save(office));
        }

        return Optional.empty();
    }

    public void delete(Long id) {
        officeRepository.deleteById(id);
    }



}
