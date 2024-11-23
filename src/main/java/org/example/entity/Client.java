package org.example.entity;

import jakarta.persistence.*;
import org.example.repositories.ClientRepository;
import org.example.service.BureaucraticServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Entity
@Component
public class Client implements Runnable {

    @Transient
    private static BureaucraticServer bureaucraticServer;

    @Transient
    private static ClientRepository clientRepository;

    @Autowired
    public void setBureaucraticServer(BureaucraticServer bureaucraticServer) {
        Client.bureaucraticServer = bureaucraticServer;
    }

    @Autowired
    public void setClientRepository(ClientRepository clientRepository) {
        Client.clientRepository = clientRepository;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String username;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "client_requested_documents",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    private List<Document> requestedDocument;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "client_owned_documents",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    private List<Document> ownedDocuments;

    public Long getId() {
        return id;
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

    public List<Document> getOwnedDocuments() {
        return ownedDocuments;
    }

    public void setOwnedDocuments(List<Document> ownedDocuments) {
        this.ownedDocuments = ownedDocuments;
    }

    public List<Document> getRequestedDocument() {
        return requestedDocument;
    }

    public void setRequestedDocument(List<Document> requestedDocument) {
        this.requestedDocument = requestedDocument;
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

    public void run() {
        bureaucraticServer.processDocumentRequest(this, requestedDocument);
        for (Document doc : requestedDocument) {
            System.out.println(name + " cu id-ul " + id + " a obtinut toate documentele necesare, inclusiv " + doc.getName());
        }
        //update clients owned documents
        this.requestedDocument.forEach(document -> {
            if (!ownedDocuments.contains(document)) {
                ownedDocuments.add(document);
            }
        });
        this.requestedDocument.clear();
        clientRepository.save(this);
//        System.out.println(name + " a obtinut toate documentele necesare, inclusiv " + requestedDocument.getName());
    }
}