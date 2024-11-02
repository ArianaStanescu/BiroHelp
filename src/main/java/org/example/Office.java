package org.example;
import java.util.*;
import java.util.concurrent.*;

class Office {
    private final String name;
    private final List<Counter> counters = new ArrayList<>();
    private final List<Document> documentTypesThatCanBeIssued = new ArrayList<>();
    private final BlockingQueue<Client> waitingClients = new LinkedBlockingQueue<>();

    public Office(String name) {
        this.name = name;
    }

    protected String getName(){
        return name;
    }

    protected boolean canIssueDocument(Document document) {
        return documentTypesThatCanBeIssued.contains(document);
    }

    protected void addCounter(Counter counter) {
        counters.add(counter);
    }

    protected void addDocuments(Document document) {
        documentTypesThatCanBeIssued.add(document);
    }

    protected void processClients() throws InterruptedException {
        Client client = waitingClients.take(); //ia urmatorul client din coada
        boolean processed = false;

        while (!processed) { //cat timp clientul nu e procesat ii cautam un ghiseu
            for (Counter counter : counters) {
                if (!counter.isBusy() && !counter.isOnBreak()) { //daca ghiseul nu e ocupat si nu e nici in pauza
                    counter.processClient(client); //trimitem clientul la ghiseul liber
                    processed = true;
                    break; //iesim ca s-a procesat clientul
                }
            }

            if (!processed) {
                Thread.sleep(1000); //daca nu si-a gasit un ghiseu liber, mai incearca peste o secunda
            }
        }
    }

    protected void addClient(Client client) throws InterruptedException {
        waitingClients.put(client); //adaugam clientul la coada
        processClients();
    }
}
