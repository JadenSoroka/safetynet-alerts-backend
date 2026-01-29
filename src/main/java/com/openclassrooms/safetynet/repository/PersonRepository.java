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
import tools.jackson.core.JacksonException;
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

  public void addPerson(Person person) throws IOException {
    try (InputStream inputStream = getClass().getClassLoader()
      .getResourceAsStream("data.json")) {
        if (inputStream == null) {
          throw new RuntimeException("data.json not found");
        }
        JsonNode rootNode = objectMapper.readTree(inputStream);
        
        ObjectNode rootObjectNode = (ObjectNode) rootNode;

        persons.add(person);
        
        rootObjectNode.putPOJO("persons", persons);
        objectMapper.writeValue(new File("src/main/resources/data.json"), rootObjectNode);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load data.json", e);
    }
  }

  public void updatePerson(Person person) throws IOException {
    try (InputStream inputStream = getClass().getClassLoader()
      .getResourceAsStream("data.json")) {
        if (inputStream == null) {
          throw new RuntimeException("data.json not found");
        }
        JsonNode rootNode = objectMapper.readTree(inputStream);
        
        ObjectNode rootObjectNode = (ObjectNode) rootNode;

        for (Person dbPerson : persons) {
          if (dbPerson.firstName().toLowerCase().equals(person.firstName()) && dbPerson.lastName().toLowerCase().equals(person.lastName())) {
            persons.remove(dbPerson);
            persons.add(person);
          }
        }
        
        rootObjectNode.putPOJO("persons", persons);
        objectMapper.writeValue(new File("src/main/resources/data.json"), rootObjectNode);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load data.json", e);
    }
  }

  public void deletePerson(Person person) throws IOException {
    try (InputStream inputStream = getClass().getClassLoader()
      .getResourceAsStream("data.json")) {
        if (inputStream == null) {
          throw new RuntimeException("data.json not found");
        }
        JsonNode rootNode = objectMapper.readTree(inputStream);
        
        ObjectNode rootObjectNode = (ObjectNode) rootNode;

        Iterator<Person> iterator = persons.iterator();
        while (iterator.hasNext()) {
          Person dbPerson = iterator.next();
          if (dbPerson.firstName().equals(person.firstName()) && dbPerson.lastName().equals(person.lastName())) {
            iterator.remove();
          }
        }
        
        rootObjectNode.putPOJO("persons", persons);
        objectMapper.writeValue(new File("src/main/resources/data.json"), rootObjectNode);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load data.json", e);
    }
  }
    
  public Person findPersonsByFirstLastName(String firstLastName) {
    Person currentPerson = null;
    for (Person person : persons) {
      if ((person.firstName().toLowerCase() + " " + person.lastName().toLowerCase()).equals(firstLastName)) {
        currentPerson = person;
      }
    }
    return currentPerson;
  }

  public Person savePerson(Person person) throws Exception{
    try {
      addPerson(person);
      System.out.println("New person added!");
      return person;
    } catch (JacksonException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Person updatePersonInfo(Person person) throws Exception {
    try {
      updatePerson(person);
      System.out.println("Person Updated: " + person);
      return person;
    } catch (JacksonException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void removePerson(Person person) throws Exception {
    try {
      deletePerson(person);
      System.out.println(person.firstName() + " " + person.lastName() + " has been deleted!");
    } catch (JacksonException e) {
      e.printStackTrace();
    }
  }

}
