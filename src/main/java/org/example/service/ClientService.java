package org.example.service;

import jakarta.transaction.Transactional;
import org.example.entity.Client;
import org.example.entity.Document;
import org.example.repositories.ClientRepository;
import org.example.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DocumentRepository documentRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client saveClient(Client client, List<Long> ownedDocumentsIds, List<Long> requestedDocumentIds) {
        List<Document> ownedDocuments = documentRepository.findAllById(ownedDocumentsIds);
        List<Document> requestedDocuments = documentRepository.findAllById(requestedDocumentIds);
        client.setOwnedDocuments(ownedDocuments);
        client.setRequestedDocument(requestedDocuments);
        return clientRepository.save(client);
    }

    public Client updateClient(Client client) {
        return clientRepository.save(client);
    }

    @Transactional
    public Client getById(Long userId) {
        return clientRepository.findById(userId).orElse(null);
    }
}
