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

import java.util.List;

class BureaucraticServer {
    private static BureaucraticServer instance;

    private BureaucraticServer() {}

    public static synchronized BureaucraticServer getInstance() {
        if (instance == null) {
            instance = new BureaucraticServer();
        }
        return instance;
    }

    public void processDocumentRequest(Client client, Document document) {
        try {

            System.out.println("Serverul proceseaza cererea pentru " + document.getName() + " de la: " + client.getName());


            // Clientul cere documentele necesare
            client.requestDocument(document);



            //Thread.sleep(2000); // Simulează timpul necesar procesării
            System.out.println("Cererea este procesata pentru " + document.getName() + " de la: " + client.getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaurează starea de întrerupere
            System.err.println("Cererea a fost intrerupta pentru " + document.getName() + " de la: " + client.getName());
        }
    }
}
