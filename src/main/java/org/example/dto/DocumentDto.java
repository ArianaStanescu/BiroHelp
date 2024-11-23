package org.example.dto;

import java.util.List;

public class DocumentDto {

    private Long id;
    private String name;
    private OfficeDto issuingOffice; // Direct biroul care emite documentul
    private List<DocumentDto> necessaryDocuments; // Direct lista documentelor necesare

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OfficeDto getIssuingOffice() {
        return issuingOffice;
    }

    public void setIssuingOffice(OfficeDto issuingOffice) {
        this.issuingOffice = issuingOffice;
    }

    public List<DocumentDto> getNecessaryDocuments() {
        return necessaryDocuments;
    }

    public void setNecessaryDocuments(List<DocumentDto> necessaryDocuments) {
        this.necessaryDocuments = necessaryDocuments;
    }
}
