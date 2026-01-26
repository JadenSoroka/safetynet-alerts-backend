package com.openclassrooms.safetynet.domain;

import java.util.List;

public record FloodPersonDTO(
  String firstName,
  String lastName,
  String phone,
  int age,
  List<String> medications,
  List<String> allergies
) {}
