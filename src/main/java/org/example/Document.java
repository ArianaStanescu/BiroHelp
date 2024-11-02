package org.example;
import java.util.*;

class Document {
    private final String name;
    private final List<Document> necessaryDocuments;
    private int clientId;

    public Document(String name) {
        this.name = name;
        this.necessaryDocuments = new ArrayList<>();
    }

    protected void setClientId(int clientId) {
        this.clientId = clientId;
    }

    protected int getClientId() {
        return clientId;
    }

    protected void addDependency(Document doc) {
        this.necessaryDocuments.add(doc);
    }

    protected List<Document> getNecessaryDocuments() {
        return necessaryDocuments;
    }

    protected String getName() {
        return name;
    }
}