package com.openclassrooms.safetynet.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.safetynet.domain.Person;
import com.openclassrooms.safetynet.service.PersonService;


@Controller
public class PersonController {
  private PersonService personService;

  public PersonController(PersonService personService) {
    this.personService = personService;
  }
  
  @GetMapping("/personInfo")
  public ResponseEntity<List<Person>> findByLastName(@RequestParam String lastName) {
    List<Person> persons = personService.findPersonByLastName(lastName);
    return ResponseEntity.ok(persons);
  }

  @GetMapping("/communityEmail")
  public ResponseEntity<List<String>> getMethodName(@RequestParam String city) {
    List<String> emails = personService.findAllEmails(city);
    return ResponseEntity.ok(emails);
  }
  



}
