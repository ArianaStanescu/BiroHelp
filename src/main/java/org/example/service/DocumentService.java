package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import org.example.entity.Client;
import org.example.entity.Counter;
import org.example.entity.Document;
import org.example.entity.Office;
import org.example.repositories.ClientRepository;
import org.example.repositories.DocumentRepository;
import org.example.repositories.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;



@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private OfficeRepository officeRepository;

    public List<Document> findAllDocuments() {
        return documentRepository.findAll();
    }

    public Optional<Document> findById(Long id) {
        return documentRepository.findById(id);
    }

    @Transactional
    public Document save(Document document, Long officeId) {
        Office office = officeRepository.findById(officeId)
                .orElseThrow(() -> new EntityNotFoundException("Office not found"));
        office.addDocuments(document);
        officeRepository.save(office);
        return document;
    }

    public Optional<Document> partialUpdate(Long id, Map<String, Object> updates) {
        Optional<Document> optionalDocument = documentRepository.findById(id);

        if (optionalDocument.isPresent()) {
            Document document = optionalDocument.get();
            boolean invalidField = false;

            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if ("name".equals(key)) {
                    document.setName((String) value);
                } else {
                    invalidField = true;
                }
            }

            if (invalidField) {
                return Optional.empty();
            }

            return Optional.of(documentRepository.save(document));
        }

        return Optional.empty();
    }

    public void addDependentDocuments(Long id, List<Long> documentIds) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));
        documentRepository.findAllById(documentIds).forEach(document::addDependency);
        documentRepository.save(document);
    }

    public void deleteDependentDocuments(Long id, List<Long> documentIds) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));
        documentRepository.findAllById(documentIds).forEach(document::removeDependency);
        documentRepository.save(document);
    }

    @Transactional
    public void delete(Long documentId) {

        Optional<Document> optionalDocument = documentRepository.findById(documentId);

        if (optionalDocument.isPresent()) {
            Document document = optionalDocument.get();
            Office office = document.getIssuingOffice();

            office.getDocumentTypesThatCanBeIssued().remove(document);

            documentRepository.flush();

            officeRepository.save(office);

            List<Client> clients = clientRepository.findAll();

            for (Client client : clients) {
                client.removeDocument(document);
                clientRepository.save(client);
            }

            clientRepository.flush();



            documentRepository.delete(document);
        }


    }
}



