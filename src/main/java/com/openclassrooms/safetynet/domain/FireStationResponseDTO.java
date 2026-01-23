package com.openclassrooms.safetynet.domain;

import java.util.List;

public record FireStationResponseDTO(
  List<CoveredPerson> coveredPersons,
  int adultCount,
  int childCount
) {}
