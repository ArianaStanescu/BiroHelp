package org.example.mapper;

import org.example.dto.OfficeDto;
import org.example.entity.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OfficeMapper {

    @Autowired
    private CounterMapper counterMapper;

    public OfficeDto mapOfficeToOfficeDto(Office office) {
        OfficeDto officeDto = new OfficeDto();
        officeDto.setId(office.getId());
        officeDto.setName(office.getName());
        officeDto.setCounters(office.getCounters().stream()
                .map(counter -> counterMapper.mapCounterToCounterDto(counter))
                .collect(Collectors.toList()));
        return officeDto;
    }

    public Office mapOfficeDtoToOffice(OfficeDto officeDto) {
        Office office = new Office();
        office.setId(officeDto.getId());
        office.setName(officeDto.getName());
        office.setCounters(officeDto.getCounters().stream()
                .map(counterDto -> counterMapper.mapCounterDtoToCounter(counterDto))
                .collect(Collectors.toList()));
        return office;
    }
}
