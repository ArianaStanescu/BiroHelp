package org.example.entity;

import jakarta.persistence.*;
import java.util.List;


@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "client_requested_documents",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    private List<Document> requestedDocument;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "client_owned_documents",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    private List<Document> ownedDocuments;

    @ManyToMany
    @JoinTable(
            name = "client_counter",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "counter_id")
    )
    private List<Counter> counters;

//    @ManyToOne
//    @JoinColumn(name = "counter_id")
//    private Counter counter;
//
//    @ManyToOne
//    @JoinColumn(name = "office_id")
//    private Office office;

    public Client() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public synchronized boolean ownsDocument(Document doc) {
        return ownedDocuments.contains(doc);
    }

    public void addOwnedDocument(Document doc) {
        ownedDocuments.add(doc);
        if (!requestedDocument.contains(doc)) {
            System.out.println(name + " a obtinut documentul intermediar " + doc.getName());
        }
    }
}