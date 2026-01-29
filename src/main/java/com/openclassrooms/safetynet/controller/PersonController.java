package com.openclassrooms.safetynet.controller;

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

  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @GetMapping("/{firstLastName}")
  public ResponseEntity<Person> getPersonByFirstLastName(@PathVariable String firstLastName) {
    Person person = personService.findPersonByFirstLastName(firstLastName);
    return ResponseEntity.ok(person);
  }
  

  @PostMapping
  public ResponseEntity<Person> createPerson(@RequestBody Person newPersonRequest) throws Exception {
    Person savedPerson = personService.createPerson(newPersonRequest);
    return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
  }

  @PutMapping("/{firstLastName}") // TODO: Fix update logic (in-place updates don't work?)
  public ResponseEntity<Person> updatePerson(@PathVariable String firstLastName, @RequestBody Person personUpdates) throws Exception {
    String correctedFullName = firstLastName.replace("_", " ").toLowerCase();
    Person person = personService.findPersonByFirstLastName(correctedFullName);
    if (person == null) {
      String errorAddOn = "";
      if (!correctedFullName.contains(" ")) {
        errorAddOn = " Make sure your url endpoint is in the format: /person/<Firstname>_<Lastname>";
      }
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, correctedFullName + " not found." + errorAddOn);
    }
    Person updatedPerson = personService.updatePersonInfo(
      new Person(person.firstName(), person.lastName(), personUpdates.address(), personUpdates.city(), personUpdates.zip(), personUpdates.phone(), personUpdates.email())
    );
    return ResponseEntity.ok(updatedPerson);
  }

  @DeleteMapping("/{firstLastName}")
  public ResponseEntity<String> deletePerson(@PathVariable String firstLastName) throws Exception {
    String correctedFullName = firstLastName.replace("_", " ").toLowerCase();
    Person person = personService.findPersonByFirstLastName(correctedFullName);
    if (person == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, firstLastName + " not found");
    }
    personService.removePerson(person);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
