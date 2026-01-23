package com.openclassrooms.safetynet.domain;

public record CoveredPerson (
  String firstName,
  String lastName,
  String address,
  String phoneNumber
) {}