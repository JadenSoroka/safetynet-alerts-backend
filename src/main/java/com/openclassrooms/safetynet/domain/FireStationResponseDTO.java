package com.openclassrooms.safetynet.domain;

import java.util.List;

public record FireStationResponseDTO(
  List<CoveredPersonDTO> coveredPersons,
  int adultCount,
  int childCount
) {}
