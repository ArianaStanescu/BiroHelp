package org.example.mapper;

import org.example.dto.CounterDto;
import org.example.entity.Counter;
import org.springframework.stereotype.Component;

@Component
public class CounterMapper {

    public CounterDto mapCounterToCounterDto(Counter counter) {
        CounterDto counterDto = new CounterDto();
        counterDto.setId(counter.getId());
        counterDto.setName(counter.getName());
        //counterDto.setBusy(counter.isBusy());
        //counterDto.setOnBreak(counter.isOnBreak());
        return counterDto;
    }

    public Counter mapCounterDtoToCounter(CounterDto counterDto) {
        Counter counter = new Counter();
        counter.setId(counterDto.getId());
        counter.setName(counterDto.getName());
        //counter.setBusy(counterDto.isBusy());
        //counter.setOnBreak(counterDto.isOnBreak());
        return counter;
    }
}
