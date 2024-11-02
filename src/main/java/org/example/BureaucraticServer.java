//package org.example;
//
//class BureaucraticServer {
//    private static BureaucraticServer instance;
//
//    //private -> constructor ca sa fie singleton
//    private BureaucraticServer() {}
//
//    public static synchronized BureaucraticServer getInstance() {
//        if (instance == null) {
//            instance = new BureaucraticServer();
//        }
//        return instance;
//    }
//
//    public synchronized void processDocumentRequest(Client client, Document document) {
//        System.out.println("Serverul proceseaza cererea pentru " + document.getName() + " de la : " + client.getName());
//        try {
//            Thread.sleep(2000); //simuleaza timpul necesar procesarii
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//        System.out.println("Cererea este procesata pentru " + document.getName() + " de la: " + client.getName());
//    }
//}



package org.example;

import java.util.*;

class BureaucraticServer {
    private static BureaucraticServer instance;
    private List<Office> offices;
//    private BureaucraticServer() {}

    private BureaucraticServer() {
        offices = new ArrayList<>();
    }
    public static synchronized BureaucraticServer getInstance() {
        if (instance == null) {
            instance = new BureaucraticServer();
        }
        return instance;
    }

    public void loadOffices(List<Office> offices) {
        this.offices = offices;
    }


    private void requestDocument(Client client, Document document) throws InterruptedException {
        Deque<Document> reverseOrder = new ArrayDeque<>();
        buildReverseDocumentList(document, reverseOrder, new HashSet<>());

        while (!reverseOrder.isEmpty()) {
            Document currentDoc = reverseOrder.poll();
            if (client.ownsDocument(currentDoc)) {
                continue;
            }

            for (Office office : offices) {
                if (office.canIssueDocument(currentDoc)) {
                    System.out.println(client.getName() + " cere documentul " + currentDoc.getName() + " la biroul " + office.getName());
                    office.addClient(client);
                    client.addOwnedDocument(currentDoc);
                    break;
                }
            }
        }
    }

    private void buildReverseDocumentList(Document document, Deque<Document> reverseOrder, Set<Document> visited) {
        if (visited.contains(document)) {
            return;
        }
        visited.add(document);

        for (Document dep : document.getNecessaryDocuments()) {
            buildReverseDocumentList(dep, reverseOrder, visited);
        }

        reverseOrder.add(document);
    }

    public void processDocumentRequest(Client client, Document document) {
        try {
            System.out.println("Serverul proceseaza cererea pentru " + document.getName() + " de la: " + client.getName());

            // Clientul cere documentele necesare
            requestDocument(client, document);

            //Thread.sleep(2000); // Simulează timpul necesar procesării
            System.out.println("Cererea este procesata pentru " + document.getName() + " de la: " + client.getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaurează starea de întrerupere
            System.err.println("Cererea a fost intrerupta pentru " + document.getName() + " de la: " + client.getName());
        }
    }
}
