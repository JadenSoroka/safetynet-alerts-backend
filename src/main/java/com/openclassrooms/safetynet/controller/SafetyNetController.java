package com.openclassrooms.safetynet.controller;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.safetynet.domain.ChildPersonDTO;
import com.openclassrooms.safetynet.domain.FireResponseDTO;
import com.openclassrooms.safetynet.domain.FireStationPersonDTO;
import com.openclassrooms.safetynet.domain.FloodResponseDTO;
import com.openclassrooms.safetynet.domain.InfoPersonDTO;
import com.openclassrooms.safetynet.service.SafetyNetService;


@RestController
public class SafetyNetController {
  private final SafetyNetService safetyNetService;

  private static final Logger LOGGER = LogManager.getLogger();

  public SafetyNetController(SafetyNetService safetyNetService) {
    this.safetyNetService = safetyNetService;
  }
  @GetMapping("/firestation")
  public ResponseEntity<FireStationPersonDTO> getPersonsByStationNumber(@RequestParam String station) {
    LOGGER.info("/firestation request for station number {}", station);
    FireStationPersonDTO fireStationResponseDTO = safetyNetService.getAllPersonsByFireStationNumber(station);
    LOGGER.info("/firestation response for station number {}: {}", station, fireStationResponseDTO);
    return ResponseEntity.ok(fireStationResponseDTO);
  }

  @GetMapping("/childAlert")
  public ResponseEntity<List<ChildPersonDTO>> getChildrenAndFamiliesByAddress(@RequestParam String address) {
    LOGGER.info("/childAlert request for address {}", address);
    List<ChildPersonDTO> childPersonDTO = safetyNetService.getChildrenAndFamiliesByAddress(address);
    LOGGER.info("/childAlert response for address {}: {}", address, childPersonDTO);
    return ResponseEntity.ok(childPersonDTO);
  }
  

  @GetMapping("/phoneAlert")
  public ResponseEntity<Set<String>> getPhoneNumbersByFireStation(@RequestParam String firestation) {
    LOGGER.info("/phoneAlert request for station number {}", firestation);
    Set<String> phoneNumbers = safetyNetService.getPhoneNumbersByFireStation(firestation);
    LOGGER.info("/childAlert response for station number {}: {}", firestation, phoneNumbers);
    return ResponseEntity.ok(phoneNumbers);
  }

  @GetMapping("/fire")
  public ResponseEntity<FireResponseDTO> getPersonsAndFireStationByAddress(@RequestParam String address) {
    LOGGER.info("/fire request for address {}", address);
    FireResponseDTO fireResponseDTO = safetyNetService.getPersonsAndFireStationByAddress(address);
    LOGGER.info("/fire response for address {}: {}", address, fireResponseDTO);
    return ResponseEntity.ok(fireResponseDTO);
  }

  @GetMapping("/flood/stations")
  public ResponseEntity<List<FloodResponseDTO>> getPersonsByStationNumbers(@RequestParam List<String> stations) {
    LOGGER.info("/flood/stations request for stations {}", stations);
    List<FloodResponseDTO> floodResponseDTO = safetyNetService.getPersonsByStationNumbers(stations);
    LOGGER.info("/flood/stations response for stations {}: {}", stations, floodResponseDTO);
    return ResponseEntity.ok(floodResponseDTO);
  }
  
  
  @GetMapping("/personInfo")
  public ResponseEntity<List<InfoPersonDTO>> findByLastName(@RequestParam String lastName) {
    LOGGER.info("/personInfo request for last name {}", lastName);
    List<InfoPersonDTO> persons = safetyNetService.findPersonsByLastName(lastName);
    LOGGER.info("/personInfo response for last name {}: {}", lastName, persons);
    return ResponseEntity.ok(persons);
  }

  @GetMapping("/communityEmail")
  public ResponseEntity<List<String>> getEmailsByCity(@RequestParam String city) {
    LOGGER.info("/communityEmail request for city {}", city);
    List<String> emails = safetyNetService.getAllEmailsByCity(city);
    LOGGER.info("/communityEmail response for city {}: {}", city, emails);
    return ResponseEntity.ok(emails);
  }
}
