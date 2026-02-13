package com.openclassrooms.safetynet.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.openclassrooms.safetynet.domain.FireStation;
import com.openclassrooms.safetynet.service.FireStationService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/firestation")
public class FireStationController {
  private final FireStationService fireStationService;

  private static final Logger LOGGER = LogManager.getLogger();

  public FireStationController(FireStationService fireStationService) {
    this.fireStationService = fireStationService;
  }

  /**
   * Retrieves a fire station mapping by address.
   * 
   * @param addressToMatch the address with underscore separator (e.g., "123_Main_St")
   * @return ResponseEntity containing the FireStation object if found
   * @throws ResponseStatusException with 404 status if the fire station is not found
   */
  @GetMapping("/{addressToMatch}")
  public ResponseEntity<FireStation> readFireStation(@PathVariable String addressToMatch) {
    String formattedAddress = addressToMatch.replace("_", " ");
    LOGGER.info("/firestation GET request for address {}", formattedAddress);

    FireStation firestation = fireStationService.readFireStation(formattedAddress);

    if (firestation == null) {
      LOGGER.error("404 /firestation GET request - Address {} not found", formattedAddress);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, formattedAddress + " not found.");
    }

    LOGGER.info("/firestation GET successful for address {}: {}", formattedAddress, firestation);
    return ResponseEntity.ok(firestation);
  }
  
  /**
   * Creates a new fire station mapping in the system.
   * 
   * @param newFireStationRequest the FireStation object containing the address and station number
   * @return ResponseEntity with 201 CREATED status containing the created FireStation object
   */
  @PostMapping
  public ResponseEntity<FireStation> AddFireStation(@RequestBody FireStation newFireStationRequest) {
    String formattedAddress = newFireStationRequest.address();
    LOGGER.info("/firestation POST request for address {}", formattedAddress);

    FireStation savedFireStation = fireStationService.createFireStation(newFireStationRequest);

    LOGGER.info("/firestation POST successful for address {}: {}", formattedAddress, savedFireStation);
    return new ResponseEntity<>(savedFireStation, HttpStatus.CREATED);
  }

  /**
   * Updates an existing fire station mapping.
   * 
   * @param address the address with underscore separator (e.g., "123_Main_St")
   * @param fireStationUpdates the FireStation object containing the updated station number
   * @return ResponseEntity with 204 NO_CONTENT status if successful
   * @throws ResponseStatusException with 404 status if the fire station is not found
   */
  @PutMapping("/{address}")
  public ResponseEntity<FireStation> UpdateFireStation(@PathVariable String address, @RequestBody FireStation fireStationUpdates) {
    String formattedAddress = address.replaceAll("_", " ");
    LOGGER.info("/firestation PUT request for address {}", formattedAddress);
    
    boolean fireStationFound = fireStationService.updateFireStation(formattedAddress, fireStationUpdates);
    if (!fireStationFound) {
      LOGGER.error("404 /firestation PUT request - Address {} not found", formattedAddress);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address at '" + formattedAddress + "' not found");
    }

    LOGGER.info("/firestation PUT successful for address {}: {}", formattedAddress, fireStationUpdates);
    return ResponseEntity.noContent().build();
  }

  /**
   * Deletes a fire station mapping from the system.
   * 
   * @param address the address with underscore separator (e.g., "123_Main_St")
   * @return ResponseEntity with 204 NO_CONTENT status if successful
   * @throws ResponseStatusException with 404 status if the fire station is not found
   */
  @DeleteMapping("/{address}")
  public ResponseEntity<FireStation> DeleteFireStation(@PathVariable String address) {
    String formattedAddress = address.replaceAll("_", " ");
    LOGGER.info("/firestation DELETE request for address {}", formattedAddress);

    boolean fireStationFound = fireStationService.deleteFireStation(formattedAddress);
    if (!fireStationFound) {
      LOGGER.error("404 /firestation DELETE request - Address {} not found", formattedAddress);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address at '" + formattedAddress + "' not found");
    }

    LOGGER.info("/firestation DELETE successful for address: {}", formattedAddress);
    return ResponseEntity.noContent().build();
  }
  
}
