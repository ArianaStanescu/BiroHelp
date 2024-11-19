package org.example.controller;

import org.example.dto.DocumentCreateDto;
import org.example.entity.Document;
import org.example.mapper.DocumentMapper;
import org.example.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private DocumentService documentService;

    // Obține toate documentele
    @GetMapping
    public List<Document> getAllDocuments() {
        return documentService.findAllDocuments();
    }

    // Obține un document după ID
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        return documentService.findById(id)
                .map(document -> ResponseEntity.ok(document))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Salvează un document nou
    @PostMapping("/{officeId}")
    public ResponseEntity<DocumentCreateDto> createDocument(@PathVariable Long officeId, @RequestBody DocumentCreateDto documentCreateDto) {
        Document documentToCreate = documentMapper.mapDocumentCreateDtoToDocument(documentCreateDto);
        Document savedDocument = documentService.save(documentToCreate, officeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(documentMapper.mapDocumentToDocumentCreateDto(savedDocument));
    }

    //adauga documentele necesare la document
    @PutMapping("/dependencies/{id}")
    public ResponseEntity<?> addDependentDocuments(@PathVariable Long id, @RequestBody List<Long> documentIds) {
         documentService.addDependentDocuments(id, documentIds);
        return ResponseEntity.noContent().build();
    }

    // Actualizează un document
    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable Long id, @RequestBody Document document) {
        return documentService.update(id, document)
                .map(updatedDocument -> ResponseEntity.ok(updatedDocument))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Șterge un document
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
