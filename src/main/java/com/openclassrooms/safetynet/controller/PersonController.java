package com.openclassrooms.safetynet.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.openclassrooms.safetynet.domain.Person;
import com.openclassrooms.safetynet.service.PersonService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.server.ResponseStatusException;



@RestController
@RequestMapping("/person")
public class PersonController {
  private final PersonService personService;

  private static final Logger LOGGER = LogManager.getLogger();

  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  /**
   * Retrieves a person by their first and last name.
   * 
   * @param firstLastNameToMatch the full name with underscore separator (e.g., "John_Doe")
   * @return ResponseEntity containing the Person object if found
   * @throws ResponseStatusException with 404 status if the person is not found
   */
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
  
  /**
   * Creates a new person in the system.
   * 
   * @param newPersonRequest the Person object containing the new person's details
   * @return ResponseEntity with 201 CREATED status containing the created Person object
   * @throws Exception if an error occurs during person creation
   */
  @PostMapping
  public ResponseEntity<Person> createPerson(@RequestBody Person newPersonRequest) throws Exception {
    String formattedFullName = newPersonRequest.firstName() + " " + newPersonRequest.lastName();
    LOGGER.info("/person POST request for name {}", formattedFullName);

    Person savedPerson = personService.createPerson(newPersonRequest);

    LOGGER.info("/person POST successful for name {}: {}", formattedFullName, savedPerson);
    return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
  }

  /**
   * Updates an existing person's information.
   * 
   * @param firstLastNameToMatch the full name with underscore separator (e.g., "John_Doe")
   * @param personUpdates the Person object containing the updated details
   * @return ResponseEntity with 204 NO_CONTENT status if successful
   * @throws Exception if an error occurs during update
   * @throws ResponseStatusException with 404 status if the person is not found
   */
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

  /**
   * Deletes a person from the system.
   * 
   * @param firstLastNameToMatch the full name with underscore separator (e.g., "John_Doe")
   * @return ResponseEntity with 204 NO_CONTENT status if successful
   * @throws Exception if an error occurs during deletion
   * @throws ResponseStatusException with 404 status if the person is not found
   */
  @DeleteMapping("/{firstLastNameToMatch}")
  public ResponseEntity<String> deletePerson(@PathVariable String firstLastNameToMatch) throws Exception {
    String formattedFullName = firstLastNameToMatch.replace("_", " ");
    LOGGER.info("/person DELETE request for name {}", formattedFullName);

    boolean personFound = personService.deletePerson(formattedFullName);

    if (!personFound) {
      LOGGER.error("404 /person DELETE request - Name {} not found", formattedFullName);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, formattedFullName + " not found");
    }

    LOGGER.info("/person DELETE successful for name {}", formattedFullName);
    return ResponseEntity.noContent().build();
  }
}
