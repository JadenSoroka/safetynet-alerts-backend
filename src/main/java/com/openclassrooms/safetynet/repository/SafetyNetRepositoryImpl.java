package com.openclassrooms.safetynet.repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.openclassrooms.safetynet.domain.FireStation;
import com.openclassrooms.safetynet.domain.MedicalRecord;
import com.openclassrooms.safetynet.domain.Person;

import jakarta.annotation.PostConstruct;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class SafetyNetRepositoryImpl implements SafetyNetRepository {
  
  private final ObjectMapper objectMapper = new ObjectMapper();
  private List<Person> persons = new ArrayList<>();
  private List<FireStation> firestations = new ArrayList<>();
  private List<MedicalRecord> medicalRecords = new ArrayList<>();
  
  @PostConstruct
  public void loadData() {
    try (InputStream inputStream = getClass().getClassLoader()
    .getResourceAsStream("data.json")) {
      if (inputStream == null) {
        throw new RuntimeException("data.json not found");
      }
      JsonNode rootNode = objectMapper.readTree(inputStream);
      JsonNode personsNode = rootNode.get("persons");
      JsonNode fireStationsNode = rootNode.get("firestations");
      JsonNode medicalRecordsNode = rootNode.get("medicalrecords");
      
      // Get people
      for (JsonNode personNode : personsNode) {
        Person person = new Person(
          personNode.get("firstName").asString(),
          personNode.get("lastName").asString(),
          personNode.get("address").asString(),
          personNode.get("city").asString(),
          personNode.get("zip").asString(),
          personNode.get("phone").asString(),
          personNode.get("email").asString()
        );
        persons.add(person);
      }

      // Get fire stations
      for (JsonNode fireStationNode : fireStationsNode) {
        FireStation firestation = new FireStation(
          fireStationNode.get("address").asString(),
          fireStationNode.get("station").asString()
        );
        firestations.add(firestation);
      }

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

  @Override
  public List<Person> findAllPersons() {
    List<Person> allPersons = new ArrayList<>();
    for (Person person : persons) {
      allPersons.add(person);
    }
    return allPersons;
  }
  
  @Override
  public List<Person> findPersonsByLastName(String lastName) {
    List<Person> currentPersons = new ArrayList<>();
    
    for (Person person : persons) {
      if (person.getLastName().equals(lastName)) {
        currentPersons.add(person);
      }
    }
    return currentPersons;
  }
  
  @Override 
  public List<Person> findPersonsByCity(String city) {
    List<Person> currentPersons = new ArrayList<>();

    for (Person person : persons) {
      if (person.getCity().equals(city)) {
        currentPersons.add(person);
      }
    }
    return currentPersons;
  }

  @Override
  public List<Person> findPersonsByAddress(String address) {
    List<Person> currentPersons = new ArrayList<>();
    for (Person person : persons) {
      if (person.getAddress().equals(address)) {
        currentPersons.add(person);
      }
    }
    return currentPersons;
  }

  @Override
  public List<FireStation> findFireStationsByStationNumber(String stationNumber) {
    List<FireStation> currentFirestations = new ArrayList<>();
    for (FireStation firestation : firestations) {
      if (firestation.getStationNumber().equals(stationNumber)) {
        currentFirestations.add(firestation);
      }
    }
    return currentFirestations;
  }

  @Override
  public List<MedicalRecord> findMedicalRecordsByFirstAndLastName(String firstName, String lastName) {
    List<MedicalRecord> currentMedicalRecords = new ArrayList<>();

    for (MedicalRecord medicalRecord : currentMedicalRecords) {
      if (medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName)) {
        currentMedicalRecords.add(medicalRecord);
      }
    }

    return currentMedicalRecords;
  }
}
