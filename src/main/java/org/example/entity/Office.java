package org.example.entity;

import jakarta.persistence.*;
import java.util.*;
import java.util.concurrent.*;

@Entity
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "office", cascade = CascadeType.ALL)
    private List<Counter> counters;

    @ManyToMany
    @JoinTable(
            name = "office_document",
            joinColumns = @JoinColumn(name = "office_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    private final List<Document> documentTypesThatCanBeIssued = new ArrayList<>();

    @OneToMany(mappedBy = "office")
    private final List<Client> clients = new ArrayList<>();

    @Transient
    private final BlockingQueue<Client> waitingClients = new LinkedBlockingQueue<>();

    public Office(String name) {
        this.name = name;
    }

    public Office() {}


    public String getName() {
        return name;
    }

    public boolean canIssueDocument(Document document) {
        return documentTypesThatCanBeIssued.contains(document);
    }

    public void addCounter(Counter counter) {
        counters.add(counter);
    }

    public void addDocuments(Document document) {
        documentTypesThatCanBeIssued.add(document);
    }

    protected void processClients() throws InterruptedException {
        Client client = waitingClients.take();
        boolean processed = false;

        while (!processed) {
            for (Counter counter : counters) {
                if (!counter.isBusy() && !counter.isOnBreak()) {
                    counter.processClient(client);
                    processed = true;
                    break;
                }
            }
        }
    }

    public void addClient(Client client) throws InterruptedException {
        waitingClients.put(client);
        processClients();
    }

    public void setId(Long id) {
        this.id = id;
    }
}
