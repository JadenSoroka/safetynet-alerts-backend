package com.openclassrooms.safetynet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.openclassrooms.safetynet.domain.FireStation;
import com.openclassrooms.safetynet.service.FireStationService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/firestation")
public class FireStationController {
  private final FireStationService fireStationService;

  public FireStationController(FireStationService fireStationService) {
    this.fireStationService = fireStationService;
  }

  // GET mapping is already satisfied in the SafetyNetController
  
  @PostMapping
  public ResponseEntity<FireStation> AddFireStation(@RequestBody FireStation newFireStationRequest) {
    FireStation newFireStation = fireStationService.createFireStation(newFireStationRequest);
    return new ResponseEntity<>(newFireStation, HttpStatus.CREATED);
  }

  @PutMapping("/{address}")
  public ResponseEntity<FireStation> UpdateFireStation(@PathVariable String address, @RequestBody FireStation updatedFireStation) {
    boolean fireStationFound = fireStationService.updateFireStation(address, updatedFireStation);
    if (!fireStationFound) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address at '" + address.replaceAll("_", " ") + "' not found");
    }
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{address}")
  public ResponseEntity<FireStation> DeleteFireStation(@PathVariable String address) {
    boolean fireStationFound = fireStationService.deleteFireStation(address);
    if (!fireStationFound) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address at '" + address.replaceAll("_", " ") + "' not found");
    }
    return ResponseEntity.noContent().build();
  }
  
}
