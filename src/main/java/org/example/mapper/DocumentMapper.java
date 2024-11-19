package org.example.mapper;

import org.example.dto.DocumentCreateDto;
import org.example.entity.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    public DocumentCreateDto mapDocumentToDocumentCreateDto(Document document) {
        DocumentCreateDto documentCreateDto = new DocumentCreateDto();
        documentCreateDto.setId(document.getId());
        documentCreateDto.setName(document.getName());
        return documentCreateDto;
    }

    public Document mapDocumentCreateDtoToDocument(DocumentCreateDto documentCreateDto) {
        Document document = new Document();
        document.setId(documentCreateDto.getId());
        document.setName(documentCreateDto.getName());
        return document;
    }
}
