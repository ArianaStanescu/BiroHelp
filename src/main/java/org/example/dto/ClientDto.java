package org.example.dto;

import java.util.List;

public class ClientDto {
    private Long id;
    private String name;
    private List<DocumentCreateDto> requestedDocuments;
    private List<DocumentCreateDto> ownedDocuments;

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

    public List<DocumentCreateDto> getRequestedDocuments() {
        return requestedDocuments;
    }

    public void setRequestedDocuments(List<DocumentCreateDto> requestedDocuments) {
        this.requestedDocuments = requestedDocuments;
    }

    public List<DocumentCreateDto> getOwnedDocuments() {
        return ownedDocuments;
    }

    public void setOwnedDocuments(List<DocumentCreateDto> ownedDocuments) {
        this.ownedDocuments = ownedDocuments;
    }
}
