package com.openclassrooms.safetynet.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.safetynet.domain.CoveredPerson;
import com.openclassrooms.safetynet.domain.Person;
import com.openclassrooms.safetynet.service.SafetyNetService;


@Controller
public class SafetyNetController {
  private SafetyNetService safetyNetService;

  public SafetyNetController(SafetyNetService safetyNetService) {
    this.safetyNetService = safetyNetService;
  }
  
  @GetMapping("/personInfo")
  public ResponseEntity<List<Person>> findByLastName(@RequestParam String lastName) {
    List<Person> persons = safetyNetService.findPersonsByLastName(lastName);
    return ResponseEntity.ok(persons);
  }

  @GetMapping("/communityEmail")
  public ResponseEntity<List<String>> getEmailsByCity(@RequestParam String city) {
    List<String> emails = safetyNetService.getAllEmailsByCity(city);
    return ResponseEntity.ok(emails);
  }

  @GetMapping("/firestation")
  public ResponseEntity<List<CoveredPerson>> getPersonsByStationNumber(@RequestParam String stationNumber) {
    List<CoveredPerson> persons = safetyNetService.getAllPersonsByFireStationNumber(stationNumber);
    return ResponseEntity.ok(persons);
  }
  
  



}
