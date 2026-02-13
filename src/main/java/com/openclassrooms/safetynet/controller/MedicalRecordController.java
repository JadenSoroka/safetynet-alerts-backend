package com.openclassrooms.safetynet.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.openclassrooms.safetynet.domain.MedicalRecord;
import com.openclassrooms.safetynet.service.MedicalRecordService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {
  private final MedicalRecordService medicalRecordService;

  private static final Logger LOGGER = LogManager.getLogger();

  public MedicalRecordController(MedicalRecordService medicalRecordService) {
    this.medicalRecordService = medicalRecordService;
  }

  /**
   * Retrieves a medical record by the person's first and last name.
   * 
   * @param firstLastNameToMatch the full name with underscore separator (e.g., "John_Doe")
   * @return ResponseEntity containing the MedicalRecord object if found
   * @throws ResponseStatusException with 404 status if the medical record is not found
   */
  @GetMapping("/{firstLastNameToMatch}")
  public ResponseEntity<MedicalRecord> readMedicalRecord(@PathVariable String firstLastNameToMatch) {
    String formattedFullName = firstLastNameToMatch.replace("_", " ");
    LOGGER.info("/medicalRecord GET request for name {}", formattedFullName);

    MedicalRecord medicalRecord = medicalRecordService.readMedicalRecord(formattedFullName);

    if (medicalRecord == null) {
      LOGGER.error("404 /medicalRecord GET request - Name {} not found", formattedFullName);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, formattedFullName + " not found.");
    }

    LOGGER.info("/medicalRecord GET successful for name {}: {}", formattedFullName, medicalRecord);
    return ResponseEntity.ok(medicalRecord);
  }
  
  /**
   * Creates a new medical record in the system.
   * 
   * @param newMedicalRecordRequest the MedicalRecord object containing the new record's details
   * @return ResponseEntity with 201 CREATED status containing the created MedicalRecord object
   */
  @PostMapping
  public ResponseEntity<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecord newMedicalRecordRequest) {
    String formattedFullName = newMedicalRecordRequest.firstName() + " " + newMedicalRecordRequest.lastName();
    LOGGER.info("/medicalRecord POST request for name {}", formattedFullName);

    MedicalRecord savedMedicalRecord = medicalRecordService.createMedicalRecord(newMedicalRecordRequest);

    LOGGER.info("/medicalRecord POST successful for name {}: {}", formattedFullName, savedMedicalRecord);
    return new ResponseEntity<>(savedMedicalRecord, HttpStatus.CREATED);
  }

  /**
   * Updates an existing medical record.
   * 
   * @param firstLastName the full name with underscore separator (e.g., "John_Doe")
   * @param medicalRecordUpdates the MedicalRecord object containing the updated details
   * @return ResponseEntity with 204 NO_CONTENT status if successful
   * @throws ResponseStatusException with 404 status if the medical record is not found
   */
  @PutMapping("/{firstLastName}")
  public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable String firstLastName, @RequestBody MedicalRecord medicalRecordUpdates) {
    String formattedFullName = firstLastName.replace("_", " ");
    LOGGER.info("/medicalRecord PUT request for name {}", formattedFullName);

    boolean medicalRecordFound = medicalRecordService.updateMedicalRecord(firstLastName, medicalRecordUpdates);
    
    if (!medicalRecordFound) {
      LOGGER.error("404 /medicalRecord PUT request - Name {} not found", formattedFullName);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Name '" + firstLastName.replaceAll("_", " ") + "' not found");
    }

    LOGGER.info("/medicalRecord PUT successful record for name {}: {}", medicalRecordUpdates);
    return ResponseEntity.noContent().build();
  }

  /**
   * Deletes a medical record from the system.
   * 
   * @param firstLastName the full name with underscore separator (e.g., "John_Doe")
   * @return ResponseEntity with 204 NO_CONTENT status if successful
   * @throws ResponseStatusException with 404 status if the medical record is not found
   */
  @DeleteMapping("/{firstLastName}")
  public ResponseEntity<MedicalRecord> deleteMedicalRecord(@PathVariable String firstLastName) {
    String formattedFullName = firstLastName.replace("_", " ");
    LOGGER.info("/medicalRecord DELETE request for name {}", formattedFullName);

    boolean medicalRecordFound = medicalRecordService.deleteMedicalRecord(firstLastName);

    if (!medicalRecordFound) {
      LOGGER.error("404 /medicalRecord DELETE request - Name {} not found");
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Name '" + firstLastName.replaceAll("_", " ") + "' not found");
    }

    LOGGER.info("/medicalRecord DELETE successful for name {}", formattedFullName);
    return ResponseEntity.noContent().build();
  }
  
}