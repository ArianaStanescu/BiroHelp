package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

//!!!!!! MUST HAVE JSON-SIMPLE MODULE LOADED TO WORK !!!!!!!!!
//!!!!!! CHECK POM.XML FILE FOR THIS DEPENDENCY !!!!!!!!!!!!!!

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//!!!!!! MUST HAVE JSON-SIMPLE MODULE LOADED TO WORK !!!!!!!!!
//!!!!!! CHECK POM.XML FILE FOR THIS DEPENDENCY !!!!!!!!!!!!!!

public class ConfigLoader {

    private Map<String, Document> documentMap = new HashMap<>();
    private List<Office> offices = new ArrayList<>();

    public void loadConfig(String filePath) {
        JSONParser parser = new JSONParser();

        try (InputStream inputStream = getClass().getResourceAsStream(filePath); InputStreamReader inputReader = new InputStreamReader(inputStream)) {

            JSONObject jsonObject = (JSONObject) parser.parse(inputReader);

            // Load Documents and Dependencies
            JSONObject documentsJson = (JSONObject) jsonObject.get("documents");
            for (Object docNameObj : documentsJson.keySet()) {
                String docName = (String) docNameObj;
                documentMap.put(docName, new Document(docName)); // Initialize Document
            }
            for (Object docNameObj : documentsJson.keySet()) {
                String docName = (String) docNameObj;
                Document document = documentMap.get(docName);
                JSONArray dependencies = (JSONArray) documentsJson.get(docName);

                for (Object depNameObj : dependencies) {
                    String depName = (String) depNameObj;
                    document.addDependency(documentMap.get(depName));
                }
            }

            // Load Offices and Counters
            JSONObject officesJson = (JSONObject) jsonObject.get("offices");
            for (Object officeNameObj : officesJson.keySet()) {
                String officeName = (String) officeNameObj;
                JSONObject officeConfig = (JSONObject) officesJson.get(officeName);

                Office office = new Office(officeName);

                // Add Counters
                JSONArray counters = (JSONArray) officeConfig.get("counters");
                for (Object counterNameObj : counters) {
                    String counterName = (String) counterNameObj;
                    office.addCounter(new Counter(counterName));
                }

                // Add Documents
                JSONArray officeDocuments = (JSONArray) officeConfig.get("documents");
                for (Object docNameObj : officeDocuments) {
                    String docName = (String) docNameObj;
                    office.addDocuments(documentMap.get(docName));
                }

                offices.add(office);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Document> getDocumentMap() {
        return documentMap;
    }

    public List<Office> getOffices() {
        return offices;
    }
}
