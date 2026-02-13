package com.openclassrooms.safetynet.repository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.openclassrooms.safetynet.domain.MedicalRecord;

import jakarta.annotation.PostConstruct;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

@Service
public class MedicalRecordRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<MedicalRecord> medicalRecords = new ArrayList<>();

    /**
     * Loads medical records from data.json file during application initialization.
     * Populates the medicalRecords list with all medical records including medications and allergies.
     * 
     * @throws RuntimeException if data.json is not found or cannot be parsed
     */
    @PostConstruct
    public void loadPersons() {
        try (InputStream inputStream = getClass().getClassLoader()
        .getResourceAsStream("data.json")) {
            if (inputStream == null) {
            throw new RuntimeException("data.json not found");
            }
            JsonNode rootNode = objectMapper.readTree(inputStream);
            JsonNode medicalRecordsNode = rootNode.get("medicalrecords");
            
            // Get medical records
            for (JsonNode medicalRecordNode : medicalRecordsNode) {
            // Get Allergies
            List<String> allergies = new ArrayList<>();
            JsonNode allergiesNode = medicalRecordNode.get("allergies");
            if (allergiesNode.isArray()) {
                for (JsonNode allergy : allergiesNode) {
                    allergies.add(allergy.asString());
                }
            }

            // Get medications
            List<String> medications = new ArrayList<>();
            JsonNode medicationsNode = medicalRecordNode.get("medications");
            if (medicationsNode.isArray()) {
                for (JsonNode medication : medicationsNode) {
                medications.add(medication.asString());
                }
            }
            
            // Put JSON object into an object in memory
            MedicalRecord medicalRecord = new MedicalRecord(
                medicalRecordNode.get("firstName").asString(),
                medicalRecordNode.get("lastName").asString(),
                medicalRecordNode.get("birthdate").asString(),
                medications,
                allergies
            );
            medicalRecords.add(medicalRecord);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load data.json", e);
        }
    }

    /**
     * Retrieves a medical record by person's full name.
     * 
     * @param firstLastNameToMatch the full name to search for (case-insensitive)
     * @return the MedicalRecord object if found, null otherwise
     */
    public MedicalRecord readPerson(String firstLastNameToMatch) {
        for (MedicalRecord dbMedicalRecord : medicalRecords) {
            String dbMedicalRecordName = dbMedicalRecord.firstName().toLowerCase() + " " + dbMedicalRecord.lastName().toLowerCase();
            if (dbMedicalRecordName.equals(firstLastNameToMatch.toLowerCase())) {
                return dbMedicalRecord;
            }
        }
        return null;
    }

    /**
     * Creates a new medical record and persists it to data.json.
     * 
     * @param newMedicalRecord the MedicalRecord object to create
     * @return the created MedicalRecord object
     * @throws RuntimeException if data.json cannot be read or written
     */
    public MedicalRecord createMedicalRecord(MedicalRecord newMedicalRecord) {
        try (InputStream inputStream = getClass().getClassLoader()
        .getResourceAsStream("data.json")) {
            if (inputStream == null) {
            throw new RuntimeException("data.json not found");
            }
            JsonNode rootNode = objectMapper.readTree(inputStream);
            
            ObjectNode rootObjectNode = (ObjectNode) rootNode;

            medicalRecords.add(newMedicalRecord);
            
            rootObjectNode.putPOJO("medicalrecords", medicalRecords);
            objectMapper.writeValue(new File("src/main/resources/data.json"), rootObjectNode);
            return newMedicalRecord;
        } catch (IOException e) {
        throw new RuntimeException("Failed to load data.json", e);
        }   
    }

    /**
     * Updates an existing medical record with new data and persists changes to data.json.
     * 
     * @param formattedFirstLastName the full name of the person whose record to update (case-insensitive)
     * @param updatedMedicalRecord the MedicalRecord object containing updated information
     * @return true if the medical record was found and updated, false otherwise
     * @throws RuntimeException if data.json cannot be read or written
     */
	public boolean updateMedicalRecord(String formattedFirstLastName, MedicalRecord updatedMedicalRecord) {
		try (InputStream inputStream = getClass().getClassLoader()
        .getResourceAsStream("data.json")) {
            if (inputStream == null) {
            throw new RuntimeException("data.json not found");
            }
            JsonNode rootNode = objectMapper.readTree(inputStream);
            
            ObjectNode rootObjectNode = (ObjectNode) rootNode;

            boolean medicalRecordFound = removeMedicalRecordFromMedicalRecords(formattedFirstLastName);
            if (!medicalRecordFound) {
                return false;
            }
            medicalRecords.add(updatedMedicalRecord);
            
            rootObjectNode.putPOJO("medicalrecords", medicalRecords);
            objectMapper.writeValue(new File("src/main/resources/data.json"), rootObjectNode);
            return true;
        } catch (IOException e) {
        throw new RuntimeException("Failed to load data.json", e);
        } 
	}
    
    /**
     * Deletes a medical record by person's full name and persists changes to data.json.
     * 
     * @param formattedFirstLastName the full name of the person whose record to delete (case-insensitive)
     * @return true if the medical record was found and deleted, false otherwise
     * @throws RuntimeException if data.json cannot be read or written
     */
    public boolean deleteMedicalRecord(String formattedFirstLastName) {
        try (InputStream inputStream = getClass().getClassLoader()
        .getResourceAsStream("data.json")) {
            if (inputStream == null) {
            throw new RuntimeException("data.json not found");
            }
            JsonNode rootNode = objectMapper.readTree(inputStream);
            
            ObjectNode rootObjectNode = (ObjectNode) rootNode;

            boolean medicalRecordFound = removeMedicalRecordFromMedicalRecords(formattedFirstLastName);
            if (!medicalRecordFound) {
                return false;
            }
            
            rootObjectNode.putPOJO("medicalrecords", medicalRecords);
            objectMapper.writeValue(new File("src/main/resources/data.json"), rootObjectNode);
            return true;
        } catch (IOException e) {
        throw new RuntimeException("Failed to load data.json", e);
        } 
	}
    
    /**
     * Helper method to remove a medical record from the in-memory list.
     * 
     * @param newFirstLastName the full name of the person whose record to remove (case-insensitive)
     * @return true if the medical record was found and removed, false otherwise
     */
    private boolean removeMedicalRecordFromMedicalRecords(String newFirstLastName) {
        Iterator<MedicalRecord> iterator = medicalRecords.iterator();
        while (iterator.hasNext()) {
            MedicalRecord dbMedicalRecord = iterator.next();
            String dbMedicalRecordName = dbMedicalRecord.firstName().toLowerCase() + " " + dbMedicalRecord.lastName().toLowerCase();
            if (dbMedicalRecordName.equals(newFirstLastName.toLowerCase())) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    
}
