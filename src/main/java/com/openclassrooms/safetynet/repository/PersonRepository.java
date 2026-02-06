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

  public Person readPerson(String firstLastNameToMatch) {
    for (Person dbPerson : persons) {
      String dbPersonName = dbPerson.firstName().toLowerCase() + " " + dbPerson.lastName().toLowerCase();
      if (dbPersonName.equals(firstLastNameToMatch.toLowerCase())) {
        return dbPerson;
      }
    }
    return null;
  }

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
    
  

  // public Person savePerson(Person person) throws Exception{
  //   try {
  //     addPerson(person);
  //     System.out.println("New person added!");
  //     return person;
  //   } catch (JacksonException e) {
  //     e.printStackTrace();
  //   }
  //   return null;
  // }

  // public Person updatePersonInfo(Person person) throws Exception {
  //   try {
  //     Person updatedPerson = updatePerson(person);
  //     return updatedPerson;
  //   } catch (JacksonException e) {
  //     e.printStackTrace();
  //   }
  //   return null;
  // }

  // public void removePerson(Person person) throws Exception {
  //   try {
  //     deletePerson(person);
  //   } catch (JacksonException e) {
  //     e.printStackTrace();
  //   }
  // }

}
