package org.example.controller;

import org.example.dto.OfficeDto;
import org.example.entity.Office;
import org.example.mapper.OfficeMapper;
import org.example.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offices")
public class OfficeController {

    @Autowired
    private OfficeMapper officeMapper;

    @Autowired
    private OfficeService officeService;

    // Obține toate birourile
    @GetMapping
    public List<Office> getAllOffices() {
        return officeService.findAllOffices();
    }

    // Obține un birou după ID
    @GetMapping("/{id}")
    public ResponseEntity<Office> getOfficeById(@PathVariable Long id) {
        return officeService.findById(id)
                .map(office -> ResponseEntity.ok(office))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Salvează un birou nou
    @PostMapping
    public ResponseEntity<OfficeDto> createOffice(@RequestBody OfficeDto officeDto) {
        Office officeToCreate = officeMapper.mapOfficeDtoToOffice(officeDto);
        Office savedOffice = officeService.save(officeToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(officeMapper.mapOfficeToOfficeDto(savedOffice));
    }

    // Actualizează un birou
    @PutMapping("/{id}")
    public ResponseEntity<Office> updateOffice(@PathVariable Long id, @RequestBody Office office) {
        return officeService.update(id, office)
                .map(updatedOffice -> ResponseEntity.ok(updatedOffice))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Șterge un birou
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffice(@PathVariable Long id) {
        officeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
