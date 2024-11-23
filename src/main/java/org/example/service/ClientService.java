package org.example.service;

import jakarta.transaction.Transactional;
import org.example.entity.Client;
import org.example.entity.Document;
import org.example.repositories.ClientRepository;
import org.example.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DocumentRepository documentRepository;

    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    public Client saveClient(Client client, List<Long> ownedDocumentsIds, List<Long> requestedDocumentIds) {
        List<Document> ownedDocuments = documentRepository.findAllById(ownedDocumentsIds);
        List<Document> requestedDocuments = documentRepository.findAllById(requestedDocumentIds);
        client.setOwnedDocuments(ownedDocuments);
        client.setRequestedDocument(requestedDocuments);
        return clientRepository.save(client);
    }

    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    public void deleteClient(Long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);

        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();

            client.getOwnedDocuments().clear();
            client.getRequestedDocument().clear();

            clientRepository.save(client);

            clientRepository.delete(client);
        } else {
            throw new IllegalArgumentException("Client with id " + id + " not found.");
        }
    }


    public Optional<Client> updateClientName(Long id, Map<String, Object> updates) {
        Optional<Client> optionalClient = clientRepository.findById(id);

        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            boolean invalidField = false;

            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if ("name".equals(key)) {
                    client.setName((String) value);
                } else {
                    invalidField = true;
                }
            }

            if (invalidField) {
                return Optional.empty();
            }

            return Optional.of(clientRepository.save(client));
        }

        return Optional.empty();
    }

    public Optional<Client> partialUpdateDocuments(Long id, Map<String, List<Long>> updates) {
        Optional<Client> optionalClient = clientRepository.findById(id);

        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();

            if (updates.containsKey("requestedDocuments")) {
                List<Long> documentIds = updates.get("requestedDocuments");
                List<Document> requestedDocuments = documentRepository.findAllById(documentIds);
                client.setRequestedDocument(requestedDocuments);
            }

            if (updates.containsKey("ownedDocuments")) {
                List<Long> documentIds = updates.get("ownedDocuments");
                List<Document> ownedDocuments = documentRepository.findAllById(documentIds);
                client.setOwnedDocuments(ownedDocuments);
            }

            return Optional.of(clientRepository.save(client));
        }

        return Optional.empty();
    }


    public Client updateClient(Client client) {
        return clientRepository.save(client);
    }

    @Transactional
    public Client getById(Long userId) {
        return clientRepository.findById(userId).orElse(null);
    }
}
