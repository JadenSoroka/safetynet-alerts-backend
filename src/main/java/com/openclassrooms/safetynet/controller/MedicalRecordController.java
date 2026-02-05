package com.openclassrooms.safetynet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.openclassrooms.safetynet.domain.MedicalRecord;
import com.openclassrooms.safetynet.service.MedicalRecordService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/medicalRecord")
public class MedicalRecordController {
  private final MedicalRecordService MedicalRecordService;

  public MedicalRecordController(MedicalRecordService MedicalRecordService) {
    this.MedicalRecordService = MedicalRecordService;
  }

  // GET mapping is already satisfied in the SafetyNetController
  
  @PostMapping
  public ResponseEntity<MedicalRecord> AddMedicalRecord(@RequestBody MedicalRecord newMedicalRecordRequest) {
    MedicalRecord newMedicalRecord = MedicalRecordService.createMedicalRecord(newMedicalRecordRequest);
    return new ResponseEntity<>(newMedicalRecord, HttpStatus.CREATED);
  }

  @PutMapping("/{firstLastName}")
  public ResponseEntity<MedicalRecord> UpdateMedicalRecord(@PathVariable String firstLastName, @RequestBody MedicalRecord updatedMedicalRecord) {
    boolean MedicalRecordFound = MedicalRecordService.updateMedicalRecord(firstLastName, updatedMedicalRecord);
    if (!MedicalRecordFound) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Name '" + firstLastName.replaceAll("_", " ") + "' not found");
    }
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{firstLastName}")
  public ResponseEntity<MedicalRecord> DeleteMedicalRecord(@PathVariable String firstLastName) {
    boolean MedicalRecordFound = MedicalRecordService.deleteMedicalRecord(firstLastName);
    if (!MedicalRecordFound) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Name '" + firstLastName.replaceAll("_", " ") + "' not found");
    }
    return ResponseEntity.noContent().build();
  }
  
}