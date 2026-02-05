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
    
    private boolean removeMedicalRecordFromMedicalRecords(String newFirstLastName) {
        boolean medicalRecordFound = false;
        Iterator<MedicalRecord> iterator = medicalRecords.iterator();
        while (iterator.hasNext()) {
            MedicalRecord dbMedicalRecord = iterator.next();
            String dbMedicalRecordName = dbMedicalRecord.firstName().toLowerCase() + " " + dbMedicalRecord.lastName().toLowerCase();
            if (dbMedicalRecordName.equals(newFirstLastName)) {
                iterator.remove();
                medicalRecordFound = true;
                break;
            }
        }
        return medicalRecordFound;
    }
}
