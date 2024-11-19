package org.example.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "document_dependencies",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "dependency_id")
    )
    private List<Document> necessaryDocuments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "office_id", nullable = false)
    private Office issuingOffice;



    public Document() {}


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

    public List<Document> getNecessaryDocuments() {
        return necessaryDocuments;
    }

    public void addDependency(Document document) {
        this.necessaryDocuments.add(document);
    }

    public void setNecessaryDocuments(List<Document> necessaryDocuments) {
        this.necessaryDocuments = necessaryDocuments;
    }

    public Office getIssuingOffice() {
        return issuingOffice;
    }

    public void setIssuingOffice(Office issuingOffice) {
        this.issuingOffice = issuingOffice;
    }
}
