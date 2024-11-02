package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        //Load config from config.json
        ConfigLoader configLoader = new ConfigLoader();
        configLoader.loadConfig("/config.json");

        //Map of documents and their dependencies
        Map<String, Document> documents = configLoader.getDocumentMap();

        List<Office> birouri = configLoader.getOffices();
        List<Client> clienti = new ArrayList<>();

        BureaucraticServer server = BureaucraticServer.getInstance();
        server.loadOffices(birouri);

        List<Document> ownedDocuments1 = new ArrayList<>();
        ownedDocuments1.add(documents.get("Certificat de Nastere"));
        ownedDocuments1.add(documents.get("Chitanta"));

        List<Document> ownedDocuments2 = new ArrayList<>();
        ownedDocuments2.add(documents.get("Certificat de Nastere"));

        List<Document> ownedDocuments3 = new ArrayList<>();

        clienti.add(new Client("Client 1", documents.get("Pasaport"), ownedDocuments1));
        clienti.add(new Client("Client 2", documents.get("Pasaport"), ownedDocuments2));
        clienti.add(new Client("Client 3", documents.get("Certificat de Casatorie"), ownedDocuments3));

        //Start threads to process clients
        for (Client client : clienti) {
            new Thread(client).start();
        }

    }
}