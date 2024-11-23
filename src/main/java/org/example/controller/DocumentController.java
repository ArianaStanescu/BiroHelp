package org.example.controller;

import org.example.dto.DocumentCreateDto;
import org.example.dto.DocumentDto;
import org.example.entity.Document;
import org.example.mapper.DocumentMapper;
import org.example.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private DocumentService documentService;

    @GetMapping
    public ResponseEntity<List<DocumentDto>> getAllDocuments() {

        List<Document> documents = documentService.findAllDocuments();

        List<DocumentDto> DocumentCreateDtos = documents.stream()
                .map(documentMapper::mapDocumentToDocumentDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(DocumentCreateDtos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<DocumentDto> getDocumentById(@PathVariable Long id) {

        Document document = documentService.findById(id).orElse(null);

        if (document != null) {
            return ResponseEntity.ok(documentMapper.mapDocumentToDocumentDto(document));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


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

    @PatchMapping("/{id}")
    public ResponseEntity<DocumentCreateDto> partialUpdateDocument(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<Document> updatedDocument = documentService.partialUpdate(id, updates);

        if (updatedDocument.isPresent()) {
            return ResponseEntity.ok(documentMapper.mapDocumentToDocumentCreateDto(updatedDocument.get()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }

    @DeleteMapping("/dependencies/{id}")
    public ResponseEntity<?> deleteDependentDocuments(@PathVariable Long id, @RequestBody List<Long> documentIds) {
        documentService.deleteDependentDocuments(id, documentIds);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id) {
        documentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
