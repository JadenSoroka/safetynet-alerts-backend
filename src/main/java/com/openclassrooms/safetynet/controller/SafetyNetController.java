package com.openclassrooms.safetynet.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.safetynet.domain.CoveredPerson;
import com.openclassrooms.safetynet.domain.FireStationResponseDTO;
import com.openclassrooms.safetynet.domain.InfoPerson;
import com.openclassrooms.safetynet.domain.Person;
import com.openclassrooms.safetynet.service.SafetyNetService;


@Controller
public class SafetyNetController {
  private SafetyNetService safetyNetService;

  public SafetyNetController(SafetyNetService safetyNetService) {
    this.safetyNetService = safetyNetService;
  }
  @GetMapping("/firestation")
  public ResponseEntity<FireStationResponseDTO> getPersonsByStationNumber(@RequestParam String stationNumber) {
    FireStationResponseDTO fireStationResponseDTO = safetyNetService.getAllPersonsByFireStationNumber(stationNumber);
    return ResponseEntity.ok(fireStationResponseDTO);
  }
  
  @GetMapping("/personInfo")
  public ResponseEntity<List<InfoPerson>> findByLastName(@RequestParam String lastName) {
    List<InfoPerson> persons = safetyNetService.findPersonsByLastName(lastName);
    return ResponseEntity.ok(persons);
  }

  @GetMapping("/communityEmail")
  public ResponseEntity<List<String>> getEmailsByCity(@RequestParam String city) {
    List<String> emails = safetyNetService.getAllEmailsByCity(city);
    return ResponseEntity.ok(emails);
  }
}
