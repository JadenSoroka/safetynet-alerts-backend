package com.openclassrooms.safetynet.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.openclassrooms.safetynet.domain.Person;
import com.openclassrooms.safetynet.service.PersonService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.server.ResponseStatusException;



@Controller
@RequestMapping("/person")
public class PersonController {
  private final PersonService personService;

  private static final Logger LOGGER = LogManager.getLogger();

  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @GetMapping("/{firstLastNameToMatch}")
  public ResponseEntity<Person> readPerson(@PathVariable String firstLastNameToMatch) {
    String formattedFullName = firstLastNameToMatch.replace("_", " ");
    LOGGER.info("/person GET request for name {}", formattedFullName);

    Person person = personService.readPerson(formattedFullName);

    if (person == null) {
      LOGGER.error("404 /person GET request - Name {} not found", formattedFullName);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, formattedFullName + " not found.");
    }

    LOGGER.info("/person GET successful for name {}: {}", formattedFullName, person);
    return ResponseEntity.ok(person);
  }
  

  @PostMapping
  public ResponseEntity<Person> createPerson(@RequestBody Person newPersonRequest) throws Exception {
    String formattedFullName = newPersonRequest.firstName() + " " + newPersonRequest.lastName();
    LOGGER.info("/person POST request for name {}", formattedFullName);

    Person savedPerson = personService.createPerson(newPersonRequest);

    LOGGER.info("/person POST successful for name {}: {}", formattedFullName, savedPerson);
    return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
  }

  @PutMapping("/{firstLastNameToMatch}")
  public ResponseEntity<Person> updatePerson(@PathVariable String firstLastNameToMatch, @RequestBody Person personUpdates) throws Exception {
    String formattedFullName = firstLastNameToMatch.replace("_", " ");
    LOGGER.info("/person PUT request for name {}", formattedFullName);

    boolean personFound = personService.updatePerson(formattedFullName, personUpdates);
    
    if (!personFound) {
      LOGGER.error("404 /person PUT request - Name {} not found", formattedFullName);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, formattedFullName + " not found.");
    }

    LOGGER.info("/person PUT successful for name {}: {}", formattedFullName, personUpdates);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{firstLastNameToMatch}")
  public ResponseEntity<String> deletePerson(@PathVariable String firstLastNameToMatch) throws Exception {
    String formattedFullName = firstLastNameToMatch.replace("_", " ");
    LOGGER.info("/person DELETE request for name {}", formattedFullName);

    boolean personFound = personService.removePerson(formattedFullName);

    if (!personFound) {
      LOGGER.error("404 /person DELETE request - Name {} not found", formattedFullName);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, formattedFullName + " not found");
    }

    LOGGER.info("/person DELETE successful for name {}", formattedFullName);
    return ResponseEntity.noContent().build();
  }
}
