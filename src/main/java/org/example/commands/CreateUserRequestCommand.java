package org.example.commands;

import org.example.entity.Document;

import java.util.List;

public class CreateUserRequestCommand extends BaseCommand {
    private Long id;
    private String name;
    private List<Document> requestedDocuments;
    private List<Document> ownedDocuments;

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

    public List<Document> getRequestedDocuments() {
        return requestedDocuments;
    }

    public void setRequestedDocuments(List<Document> requestedDocuments) {
        this.requestedDocuments = requestedDocuments;
    }

    public List<Document> getOwnedDocuments() {
        return ownedDocuments;
    }

    public void setOwnedDocuments(List<Document> ownedDocuments) {
        this.ownedDocuments = ownedDocuments;
    }
}
