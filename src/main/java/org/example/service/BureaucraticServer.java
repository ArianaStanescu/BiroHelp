package org.example.service;

import org.example.entity.Client;
import org.example.entity.Document;
import org.example.entity.Office;
import org.example.repositories.OfficeRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BureaucraticServer {

    private final List<Office> offices;

    public BureaucraticServer(OfficeRepository officeRepository) {
        offices = officeRepository.findAll();
    }

    public void requestDocument(Client client, Document document) throws InterruptedException {
        Deque<Document> reverseOrder = new ArrayDeque<>();
        buildReverseDocumentList(document, reverseOrder, new HashSet<>());


        while (!reverseOrder.isEmpty()) {
            Document currentDoc = reverseOrder.poll();

            if (client.ownsDocument(currentDoc)) {
                System.out.println("Clientul detine deja documentul: " + currentDoc.getName());
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

    public void processDocumentRequest(Client client, List<Document> documents) {
        for(Document document : documents) {
            try {
                System.out.println("Serverul proceseaza cererea pentru " + document.getName() + " de la: " + client.getName());
                // Clientul cere documentele necesare
                requestDocument(client, document);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restaurează starea de întrerupere
                System.err.println("Cererea a fost intrerupta pentru " + document.getName() + " de la: " + client.getName());
            }
        }
    }
}
