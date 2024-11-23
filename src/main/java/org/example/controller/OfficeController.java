package org.example.controller;

import org.example.dto.OfficeDto;
import org.example.dto.OfficeGetDto;
import org.example.entity.Office;
import org.example.mapper.OfficeMapper;
import org.example.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;

@RestController
@RequestMapping("/offices")
public class OfficeController {

    @Autowired
    private OfficeMapper officeMapper;

    @Autowired
    private OfficeService officeService;


    @GetMapping
    public ResponseEntity<List<OfficeGetDto>> getAllOffices() {

        List<Office> offices = officeService.findAllOffices();

        List<OfficeGetDto> officeGetDtos = offices.stream()
                .map(officeMapper::mapOfficeToOfficeGetDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(officeGetDtos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<OfficeGetDto> getOfficeById(@PathVariable Long id) {

        Office office = officeService.findById(id).orElse(null);

        if (office != null) {
            return ResponseEntity.ok(officeMapper.mapOfficeToOfficeGetDto(office));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }


    @PostMapping
    public ResponseEntity<OfficeDto> createOffice(@RequestBody OfficeDto officeDto) {
        Office officeToCreate = officeMapper.mapOfficeDtoToOffice(officeDto);
        Office savedOffice = officeService.save(officeToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(officeMapper.mapOfficeToOfficeDto(savedOffice));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<OfficeDto> partialUpdateOffice(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<Office> updatedOffice = officeService.partialUpdate(id, updates);

        if (updatedOffice.isPresent()) {
            return ResponseEntity.ok(officeMapper.mapOfficeToOfficeDto(updatedOffice.get()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffice(@PathVariable Long id) {
        officeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
