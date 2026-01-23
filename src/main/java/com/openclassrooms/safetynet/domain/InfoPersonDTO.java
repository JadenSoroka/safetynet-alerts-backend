package com.openclassrooms.safetynet.domain;

import java.util.List;

public record InfoPersonDTO (
  String firstName,
  String lastName,
  String email,
  List<String> medications,
  List<String> allergies
) {}