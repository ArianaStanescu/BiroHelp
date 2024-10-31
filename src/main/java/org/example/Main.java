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

        clienti.add(new Client("Client 1", documents.get("Buletin"), birouri));
        clienti.add(new Client("Client 2", documents.get("Pasaport"), birouri));

        //Start threads to process clients
        for (Client client : clienti) {
            new Thread(client).start();
        }

    }
}