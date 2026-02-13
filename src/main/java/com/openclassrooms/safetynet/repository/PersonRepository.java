package com.openclassrooms.safetynet.repository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.openclassrooms.safetynet.domain.Person;

import jakarta.annotation.PostConstruct;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

@Service
public class PersonRepository {
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final List<Person> persons = new ArrayList<>();
  
  /**
   * Loads person data from data.json file during application initialization.
   * Populates the persons list with all person records.
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
      JsonNode personsNode = rootNode.get("persons");
      
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

    } catch (IOException e) {
      throw new RuntimeException("Failed to load data.json", e);
    }
  }

  /**
   * Retrieves a person by their full name.
   * 
   * @param firstLastNameToMatch the full name to search for (case-insensitive)
   * @return the Person object if found, null otherwise
   */
  public Person readPerson(String firstLastNameToMatch) {
    for (Person dbPerson : persons) {
      String dbPersonName = dbPerson.firstName().toLowerCase() + " " + dbPerson.lastName().toLowerCase();
      if (dbPersonName.equals(firstLastNameToMatch.toLowerCase())) {
        return dbPerson;
      }
    }
    return null;
  }

  /**
   * Creates a new person and persists it to data.json.
   * 
   * @param newPerson the Person object to create
   * @return the created Person object
   * @throws IOException if an I/O error occurs
   * @throws RuntimeException if data.json cannot be read or written
   */
  public Person createPerson(Person newPerson) throws IOException {
    try (InputStream inputStream = getClass().getClassLoader()
      .getResourceAsStream("data.json")) {
        if (inputStream == null) {
          throw new RuntimeException("data.json not found");
        }
        JsonNode rootNode = objectMapper.readTree(inputStream);
        
        ObjectNode rootObjectNode = (ObjectNode) rootNode;

        persons.add(newPerson);
        
        rootObjectNode.putPOJO("persons", persons);
        objectMapper.writeValue(new File("src/main/resources/data.json"), rootObjectNode);
        return newPerson;
    } catch (IOException e) {
      throw new RuntimeException("Failed to load data.json", e);
    }
  }

  /**
   * Updates an existing person with new data and persists changes to data.json.
   * 
   * @param firstLastNameToMatch the full name of the person to update (case-insensitive)
   * @param newPerson the Person object containing updated information
   * @return true if the person was found and updated, false otherwise
   * @throws IOException if an I/O error occurs
   * @throws RuntimeException if data.json cannot be read or written
   */
  public boolean updatePerson(String firstLastNameToMatch, Person newPerson) throws IOException {
    try (InputStream inputStream = getClass().getClassLoader()
      .getResourceAsStream("data.json")) {
        if (inputStream == null) {
          throw new RuntimeException("data.json not found");
        }
        JsonNode rootNode = objectMapper.readTree(inputStream);
        
        ObjectNode rootObjectNode = (ObjectNode) rootNode;

        boolean personFound = removePersonFromPersons(firstLastNameToMatch);
        if (!personFound) {
          return false;
        }
        persons.add(newPerson);
        
        rootObjectNode.putPOJO("persons", persons);
        objectMapper.writeValue(new File("src/main/resources/data.json"), rootObjectNode);
        return true;
    } catch (IOException e) {
      throw new RuntimeException("Failed to load data.json", e);
    }
  }

  /**
   * Deletes a person by their full name and persists changes to data.json.
   * 
   * @param firstLastNameToMatch the full name of the person to delete (case-insensitive)
   * @return true if the person was found and deleted, false otherwise
   * @throws IOException if an I/O error occurs
   * @throws RuntimeException if data.json cannot be read or written
   */
  public boolean deletePerson(String firstLastNameToMatch) throws IOException {
    try (InputStream inputStream = getClass().getClassLoader()
      .getResourceAsStream("data.json")) {
        if (inputStream == null) {
          throw new RuntimeException("data.json not found");
        }
        JsonNode rootNode = objectMapper.readTree(inputStream);
        
        ObjectNode rootObjectNode = (ObjectNode) rootNode;

        boolean personFound = removePersonFromPersons(firstLastNameToMatch);
        if (!personFound) {
          return false;
        }
        
        rootObjectNode.putPOJO("persons", persons);
        objectMapper.writeValue(new File("src/main/resources/data.json"), rootObjectNode);
        return true;
    } catch (IOException e) {
      throw new RuntimeException("Failed to load data.json", e);
    }
  }

  /**
   * Helper method to remove a person from the in-memory list.
   * 
   * @param oldFirstLastName the full name of the person to remove (case-insensitive)
   * @return true if the person was found and removed, false otherwise
   */
  private boolean removePersonFromPersons(String oldFirstLastName) {
    Iterator<Person> iterator = persons.iterator();
    while (iterator.hasNext()) {
      Person dbPerson = iterator.next();
      String dbPersonName = dbPerson.firstName().toLowerCase() + " " + dbPerson.lastName().toLowerCase();
      if (dbPersonName.equals(oldFirstLastName.toLowerCase())) {
        iterator.remove();
        return true;
      }
    }
    return false;
  }
}
