package com.openclassrooms.safetynet.controller;

import org.springframework.stereotype.Controller;

import com.openclassrooms.safetynet.repository.SafetyNetRepository;

@Controller
public class MedicalRecordController {
  private final SafetyNetRepository safetyNetRepository;

  public MedicalRecordController(SafetyNetRepository safetyNetRepository) {
    this.safetyNetRepository = safetyNetRepository;
  }
}
