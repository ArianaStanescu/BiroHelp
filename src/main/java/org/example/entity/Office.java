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

    @OneToMany(mappedBy = "office", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Counter> counters;


    @OneToMany(mappedBy = "issuingOffice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private final List<Document> documentTypesThatCanBeIssued = new ArrayList<>();


//    @OneToMany(mappedBy = "office")
//    private final List<Client> clients = new ArrayList<>();

    @Transient
    private final BlockingQueue<Client> waitingClients = new LinkedBlockingQueue<>();


    public Office() {}


    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public List<Counter> getCounters() {
        return counters;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCounters(List<Counter> counters) {
        this.counters = counters;
    }

    public List<Document> getDocumentTypesThatCanBeIssued() {
        return documentTypesThatCanBeIssued;
    }


    public boolean canIssueDocument(Document document) {
        return documentTypesThatCanBeIssued.contains(document);
    }

    public void addCounter(Counter counter) {
        counters.add(counter);
    }

    public void addDocuments(Document document) {
        documentTypesThatCanBeIssued.add(document);
        document.setIssuingOffice(this);
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

    public void addCounters(Counter counter) {
        counters.add(counter);
        counter.setOffice(this);
    }
}
