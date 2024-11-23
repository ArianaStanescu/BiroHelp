// src/main/java/org/example/dto/ClientUpdateDocumentsDto.java
package org.example.dto;

import java.util.List;

public class ClientUpdateDocumentsDto {
    private List<Long> requestedDocumentIds;
    private List<Long> ownedDocumentsIds;

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