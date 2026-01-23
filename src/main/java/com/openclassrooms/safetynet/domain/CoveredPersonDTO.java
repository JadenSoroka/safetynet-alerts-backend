package com.openclassrooms.safetynet.domain;

public record CoveredPersonDTO (
  String firstName,
  String lastName,
  String address,
  String phoneNumber
) {}