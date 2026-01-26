package com.openclassrooms.safetynet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.safetynet.service.SafetyNetService;

@Controller
public class FireStationController {
  private final SafetyNetService safetyNetService;

  public FireStationController(SafetyNetService safetyNetService) {
    this.safetyNetService = safetyNetService;
  }
  
}
