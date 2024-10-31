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

    private BureaucraticServer() {}

    public static synchronized BureaucraticServer getInstance() {
        if (instance == null) {
            instance = new BureaucraticServer();
        }
        return instance;
    }
//
//    protected void requestDocument(Document document) throws InterruptedException {
//        Deque<Document> reverseOrder = new ArrayDeque<>(); // Coada inversata de documente - ne asiguram ca documentele necesare sunt obtinute in ordinea corecta
//        buildReverseDocumentList(document, reverseOrder, new HashSet<>()); // Construim lista inversata
//
//        while (!reverseOrder.isEmpty()) {
//            Document currentDoc = reverseOrder.poll(); // Scoatem primul document de la coada
//            if (ownsDocument(currentDoc)) {
//                continue; // Daca clientul deține deja documentul trece la urmatorul
//            }
//
//            // Verificam daca vreun birou poate elibera documentul curent
//            for (Office office : offices) {
//                if (office.canIssueDocument(currentDoc)) { // Daca biroul poate elibera documentul
//                    System.out.println(name + " cere documentul " + currentDoc.getName() + " la biroul " + office.getName());
//                    office.addClient(this); // Adauga clientul in coada biroului
//                    ownedDocuments.add(currentDoc); // Adauga documentul la lista de documente deținute
//                    break;
//                }
//            }
//        }
//    }
//
//    // Metoda recursiva care construiește lista de documente in ordine inversata
//    private void buildReverseDocumentList(Document document, Deque<Document> reverseOrder, Set<Document> visited) {
//        if (visited.contains(document)) {
//            return; // Evitam recursivitatea infinita
//        }
//        visited.add(document);
//
//        for (Document dep : document.getNecessaryDocuments()) {
//            buildReverseDocumentList(dep, reverseOrder, visited); // Apel recursiv pentru fiecare document necesar
//        }
//
//        reverseOrder.add(document); // Se adauga doc curent la finalul listei si se incepe cu documentul cel mai de baza
//    }

    private void requestDocument(Client client, Document document) throws InterruptedException {
        Deque<Document> reverseOrder = new ArrayDeque<>();
        buildReverseDocumentList(document, reverseOrder, new HashSet<>());

        while (!reverseOrder.isEmpty()) {
            Document currentDoc = reverseOrder.poll();
            if (client.ownsDocument(currentDoc)) {
                continue;
            }

            for (Office office : client.getOffices()) {
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
