package com.openclassrooms.safetynet.domain;

import java.util.List;

public record FirePersonDTO(
  String fireStationNumber,
  String firstName,
  String lastName,
  String phoneNumber,
  List<String> medications,
  List<String> allergies
) {}
