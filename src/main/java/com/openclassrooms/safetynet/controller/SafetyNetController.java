package com.openclassrooms.safetynet.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.safetynet.domain.FireResponseDTO;
import com.openclassrooms.safetynet.domain.FireStationPersonDTO;
import com.openclassrooms.safetynet.domain.FloodResponseDTO;
import com.openclassrooms.safetynet.domain.InfoPersonDTO;
import com.openclassrooms.safetynet.service.SafetyNetService;


@Controller
public class SafetyNetController {
  private final SafetyNetService safetyNetService;

  public SafetyNetController(SafetyNetService safetyNetService) {
    this.safetyNetService = safetyNetService;
  }
  @GetMapping("/firestation")
  public ResponseEntity<FireStationPersonDTO> getPersonsByStationNumber(@RequestParam String stationNumber) {
    FireStationPersonDTO fireStationResponseDTO = safetyNetService.getAllPersonsByFireStationNumber(stationNumber);
    return ResponseEntity.ok(fireStationResponseDTO);
  }

  @GetMapping("/phoneAlert")
  public ResponseEntity<Set<String>> getPhoneNumbersByFireStation(@RequestParam String firestation) {
    Set<String> phoneNumbers = safetyNetService.getPhoneNumbersByFireStation(firestation);
    return ResponseEntity.ok(phoneNumbers);
  }

  @GetMapping("/fire")
  public ResponseEntity<FireResponseDTO> getPersonsAndFireStationByAddress(@RequestParam String address) {
    FireResponseDTO fireResponseDTO = safetyNetService.getPersonsAndFireStationByAddress(address);
    return ResponseEntity.ok(fireResponseDTO);
  }

  @GetMapping("/flood/stations")
  public ResponseEntity<List<FloodResponseDTO>> getPersonsByStationNumbers(@RequestParam List<String> stations) {
    List<FloodResponseDTO> floodResponse = safetyNetService.getPersonsByStationNumbers(stations);
    return ResponseEntity.ok(floodResponse);
  }
  
  
  @GetMapping("/personInfo")
  public ResponseEntity<List<InfoPersonDTO>> findByLastName(@RequestParam String lastName) {
    List<InfoPersonDTO> persons = safetyNetService.findPersonsByLastName(lastName);
    return ResponseEntity.ok(persons);
  }

  @GetMapping("/communityEmail")
  public ResponseEntity<List<String>> getEmailsByCity(@RequestParam String city) {
    List<String> emails = safetyNetService.getAllEmailsByCity(city);
    return ResponseEntity.ok(emails);
  }
}
