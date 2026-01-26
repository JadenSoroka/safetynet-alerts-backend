package com.openclassrooms.safetynet.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.safetynet.domain.InfoPersonDTO;
import com.openclassrooms.safetynet.service.SafetyNetService;

@Controller
public class PersonController {
private final SafetyNetService safetyNetService;

  public PersonController(SafetyNetService safetyNetService) {
    this.safetyNetService = safetyNetService;
  }

  @GetMapping("/person")
  public ResponseEntity<List<InfoPersonDTO>> findByLastName(@RequestParam String lastName) {
    List<InfoPersonDTO> persons = safetyNetService.findPersonsByLastName(lastName);
    return ResponseEntity.ok(persons);
  }
}
