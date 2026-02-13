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
public class SafetyNetRepository {
  
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final List<Person> persons = new ArrayList<>();
  private final List<FireStation> firestations = new ArrayList<>();
  private final List<MedicalRecord> medicalRecords = new ArrayList<>();
  
  /**
   * Loads all data (persons, fire stations, and medical records) from data.json during application initialization.
   * 
   * @throws RuntimeException if data.json is not found or cannot be parsed
   */
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
      
      getPersons(personsNode);

      getFireStations(fireStationsNode);

      getMedicalRecords(medicalRecordsNode);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load data.json", e);
    }
  }

  /**
   * Helper method to parse and load medical records from JSON data.
   * Extracts medications and allergies for each medical record.
   * 
   * @param medicalRecordsNode the JSON node containing medical records data
   */
  private void getMedicalRecords(JsonNode medicalRecordsNode) {
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
  }

  /**
   * Helper method to parse and load fire station mappings from JSON data.
   * 
   * @param fireStationsNode the JSON node containing fire stations data
   */
  private void getFireStations(JsonNode fireStationsNode) {
    // Get fire stations
    for (JsonNode fireStationNode : fireStationsNode) {
      FireStation firestation = new FireStation(
        fireStationNode.get("address").asString(),
        fireStationNode.get("station").asString()
      );
      firestations.add(firestation);
    }
  }

  /**
   * Helper method to parse and load person data from JSON data.
   * 
   * @param personsNode the JSON node containing persons data
   */
  private void getPersons(JsonNode personsNode) {
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
  }

  /**
   * Retrieves all persons from the repository.
   * 
   * @return a list containing all Person objects
   */
  public List<Person> findAllPersons() {
    List<Person> allPersons = new ArrayList<>();
    for (Person person : persons) {
      allPersons.add(person);
    }
    return allPersons;
  }
  
  /**
   * Finds all persons with a specific last name.
   * 
   * @param lastName the last name to search for
   * @return a list of Person objects matching the last name
   */
  public List<Person> findPersonsByLastName(String lastName) {
    List<Person> currentPersons = new ArrayList<>();
    
    for (Person person : persons) {
      if (person.lastName().equals(lastName)) {
        currentPersons.add(person);
      }
    }
    return currentPersons;
  }
  
  /**
   * Finds all persons living in a specific city.
   * 
   * @param city the city name to search for
   * @return a list of Person objects in the specified city
   */
  public List<Person> findPersonsByCity(String city) {
    List<Person> currentPersons = new ArrayList<>();

    for (Person person : persons) {
      if (person.city().equals(city)) {
        currentPersons.add(person);
      }
    }
    return currentPersons;
  }

  /**
   * Finds all persons living at a specific address.
   * 
   * @param address the address to search for
   * @return a list of Person objects at the specified address
   */
  public List<Person> findPersonsByAddress(String address) {
    List<Person> currentPersons = new ArrayList<>();
    for (Person person : persons) {
      if (person.address().equals(address)) {
        currentPersons.add(person);
      }
    }
    return currentPersons;
  }

  /**
   * Finds all fire station mappings for a specific station number.
   * 
   * @param stationNumber the fire station number to search for
   * @return a list of FireStation objects with the specified station number
   */
  public List<FireStation> findFireStationsByStationNumber(String stationNumber) {
    List<FireStation> currentFirestations = new ArrayList<>();
    for (FireStation firestation : firestations) {
      if (firestation.station().equals(stationNumber)) {
        currentFirestations.add(firestation);
      }
    }
    return currentFirestations;
  }

  /**
   * Finds the fire station mapping for a specific address.
   * 
   * @param address the address to search for
   * @return the FireStation object if found, null otherwise
   */
  public FireStation findFireStationByAddress(String address) {
    for (FireStation fireStation : firestations) {
      if (fireStation.address().equals(address)) {
        return fireStation;
      }
    }
    return null;
  }

  /**
   * Finds a medical record by first and last name.
   * 
   * @param firstName the first name to search for
   * @param lastName the last name to search for
   * @return the MedicalRecord object if found, null otherwise
   */
  public MedicalRecord findMedicalRecordsByFirstAndLastName(String firstName, String lastName) {
    MedicalRecord currentMedicalRecords = null;

    for (MedicalRecord medicalRecord : medicalRecords) {
      if (medicalRecord.firstName().equals(firstName) && medicalRecord.lastName().equals(lastName)) {
        currentMedicalRecords = medicalRecord;
      }
    }

    return currentMedicalRecords;
  }
}
