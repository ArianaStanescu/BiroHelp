package org.example.mapper;

import org.example.dto.DocumentCreateDto;
import org.example.dto.DocumentDto;
import org.example.entity.Document;
import org.example.entity.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DocumentMapper {

    @Autowired
    private OfficeMapper officeMapper;

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

    public DocumentDto mapDocumentToDocumentDto(Document document) {
        DocumentDto documentDto = new DocumentDto();
        documentDto.setId(document.getId());
        documentDto.setName(document.getName());
        documentDto.setIssuingOffice(officeMapper.mapOfficeToOfficeDto(document.getIssuingOffice())); // Mapează biroul
        documentDto.setNecessaryDocuments(document.getNecessaryDocuments().stream()
                .map(this::mapDocumentToDocumentDto) // Mapează fiecare document necesar
                .collect(Collectors.toList()));
        return documentDto;
    }

    public Document mapDocumentDtoToDocument(DocumentDto documentDto) {
        Document document = new Document();
        document.setId(documentDto.getId());
        document.setName(documentDto.getName());
        document.setIssuingOffice(officeMapper.mapOfficeDtoToOffice(documentDto.getIssuingOffice())); // Mapează biroul
        document.setNecessaryDocuments(documentDto.getNecessaryDocuments().stream()
                .map(this::mapDocumentDtoToDocument) // Mapează fiecare document necesar
                .collect(Collectors.toList()));
        return document;
    }


}
