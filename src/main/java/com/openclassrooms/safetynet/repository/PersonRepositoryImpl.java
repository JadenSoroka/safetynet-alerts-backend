package com.openclassrooms.safetynet.repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.openclassrooms.safetynet.domain.Person;

import jakarta.annotation.PostConstruct;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class PersonRepositoryImpl implements PersonRepository {
  
  private final ObjectMapper objectMapper = new ObjectMapper();
  private List<Person> persons = new ArrayList<>();
  private List<Person> currentPersons = new ArrayList<>();

  @PostConstruct
  public void loadData() {
    try (InputStream inputStream = getClass().getClassLoader()
        .getResourceAsStream("data.json")) {
      if (inputStream == null) {
        throw new RuntimeException("data.json not found");
      }
      JsonNode rootNode = objectMapper.readTree(inputStream);
      JsonNode personsNode = rootNode.get("persons");
      
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

  @Override
  public List<Person> findByLastName(String lastName) {
    for (Person person : persons) {
      if (person.getLastName().equals(lastName)) {
        currentPersons.add(person);
      }
    }
    return currentPersons;
  }
}
