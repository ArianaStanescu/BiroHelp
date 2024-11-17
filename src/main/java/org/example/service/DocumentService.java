package org.example.service;

import org.example.entity.Document;
import org.example.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public List<Document> findAllDocuments() {
        return documentRepository.findAll();
    }

    public Optional<Document> findById(Long id) {
        return documentRepository.findById(id);
    }

    public Document save(Document document) {
        return documentRepository.save(document);
    }

    public Optional<Document> update(Long id, Document document) {
        if (documentRepository.existsById(id)) {
            document.setId(id);
            return Optional.of(documentRepository.save(document));
        }
        return Optional.empty();
    }

    public void delete(Long id) {
        documentRepository.deleteById(id);
    }
}
