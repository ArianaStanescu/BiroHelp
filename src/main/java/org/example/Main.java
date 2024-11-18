//package org.example;
//
//import org.example.entity.Client;
//import org.example.entity.Document;
//import org.example.entity.Office;
//import org.example.service.BureaucraticServer;
//
//import java.util.*;
//
//public class Main {
//    public static void main(String[] args) throws InterruptedException {
//
//        //Load config from config.json
//        /ConfigLoader configLoader = new ConfigLoader();
//        configLoader.loadConfig("/config.json");
//
//        //Map of documents and their dependencies
//        Map<String, Document> documents = configLoader.getDocumentMap();
//        List<Office> birouri = configLoader.getOffices();
//        List<Client> clienti = configLoader.getClients();
//
//        BureaucraticServer server = BureaucraticServer.getInstance();
//        server.loadOffices(birouri);
//
//        //Start threads to process clients
////        for (Client client : clienti) {
////            new Thread(client).start();
////        }
//    }
//}
