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


        // client 1: cere doua documente, are deja doua documente
        List<Document> ownedDocuments1 = new ArrayList<>();
        ownedDocuments1.add(documents.get("Certificat de Nastere"));
        ownedDocuments1.add(documents.get("Chitanta"));

        List<Document> requestedDocuments1 = new ArrayList<>();
        requestedDocuments1.add(documents.get("Pasaport"));
        requestedDocuments1.add(documents.get("Permis de Conducere"));

        clienti.add(new Client("Client 1", requestedDocuments1, ownedDocuments1));


        // client 2: cere un document, are deja un document
        List<Document> ownedDocuments2 = new ArrayList<>();
        ownedDocuments2.add(documents.get("Certificat de Nastere"));

        List<Document> requestedDocuments2 = new ArrayList<>();
        requestedDocuments2.add(documents.get("Certificat de Casatorie"));

        clienti.add(new Client("Client 2", requestedDocuments2, ownedDocuments2));



        // client 3: cere un document, nu are niciun document
        List<Document> ownedDocuments3 = new ArrayList<>();

        List<Document> requestedDocuments3 = new ArrayList<>();
        requestedDocuments3.add(documents.get("Permis de Conducere"));

        clienti.add(new Client("Client 3", requestedDocuments3, ownedDocuments3));


        //Start threads to process clients
        for (Client client : clienti) {
            new Thread(client).start();
        }

    }
}