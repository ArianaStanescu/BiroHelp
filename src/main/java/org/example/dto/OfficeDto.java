package org.example.dto;

import java.util.List;

public class OfficeDto {
    private Long id;
    private String name;
    private List<CounterDto> counters;
//    private final List<Document> documentTypesThatCanBeIssued = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CounterDto> getCounters() {
        return counters;
    }

    public void setCounters(List<CounterDto> counters) {
        this.counters = counters;
    }
}
