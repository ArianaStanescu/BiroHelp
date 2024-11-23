package org.example.dto;

import java.util.List;

public class ClientCreateDto {
    private Long id;
    private String name;
    private String username;
    private List<Long> requestedDocumentIds;
    private List<Long> ownedDocumentsIds;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Long> getRequestedDocumentIds() {
        return requestedDocumentIds;
    }

    public void setRequestedDocumentIds(List<Long> requestedDocumentIds) {
        this.requestedDocumentIds = requestedDocumentIds;
    }

    public List<Long> getOwnedDocumentsIds() {
        return ownedDocumentsIds;
    }

    public void setOwnedDocumentsIds(List<Long> ownedDocumentsIds) {
        this.ownedDocumentsIds = ownedDocumentsIds;
    }
}
